package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import modelo.Calidad;
import modelo.Suscripcion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba de integracion real contra SQLite (db/streamflow.db), porque
 * SuscripcionDAOImpl no permite inyectar otra URL.
 *
 * SuscripcionDAO no expone ningun metodo eliminar() por diseno (una suscripcion
 * es un registro historico/de facturacion, no algo que se borre en un sistema
 * real). Para no dejar basura de prueba en la base de todos modos, este test se
 * limpia a si mismo con una conexion JDBC propia y directa (fuera de
 * SuscripcionDAO/SuscripcionDAOImpl, sin tocar ni una linea de src/): cada test
 * registra los emails unicos que crea, y el @AfterEach los borra por su cuenta.
 */
class SuscripcionDAOImplTest {

    private static final String URL_BASE_REAL = "jdbc:sqlite:db/streamflow.db";

    private final SuscripcionDAO dao = new SuscripcionDAOImpl();
    private final List<String> emailsDePrueba = new ArrayList<>();

    @AfterEach
    void limpiarRegistrosDePrueba() throws SQLException {
        String sql = "DELETE FROM suscripcion WHERE usuario_email = ?";
        try (Connection conn = DriverManager.getConnection(URL_BASE_REAL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String email : emailsDePrueba) {
                pstmt.setString(1, email);
                pstmt.executeUpdate();
            }
        }
        emailsDePrueba.clear();
    }

    @Test
    void guardarPersisteLaSuscripcion() {
        String emailUnico = emailDePrueba();
        Suscripcion suscripcion = new Suscripcion(emailUnico, Calidad.HD, 8.0, "2026-07-20");

        assertTrue(dao.guardar(suscripcion));
        assertEquals(1, dao.listarPorUsuario(emailUnico).size());
    }

    @Test
    void listarPorUsuarioDevuelveSoloLasDeEseUsuario() {
        String emailA = emailDePrueba();
        String emailB = emailDePrueba();
        dao.guardar(new Suscripcion(emailA, Calidad.HD, 8.0, "2026-07-20"));
        dao.guardar(new Suscripcion(emailB, Calidad.SD, 5.0, "2026-07-20"));

        List<Suscripcion> deA = dao.listarPorUsuario(emailA);

        assertEquals(1, deA.size());
        assertEquals(emailA, deA.get(0).getUsuarioEmail());
    }

    @Test
    void buscarUltimaPorUsuarioDevuelveLaMasReciente() {
        String emailUnico = emailDePrueba();
        dao.guardar(new Suscripcion(emailUnico, Calidad.SD, 5.0, "2026-01-01"));
        dao.guardar(new Suscripcion(emailUnico, Calidad.UHD_4K, 12.0, "2026-07-20"));

        Suscripcion vigente = dao.buscarUltimaPorUsuario(emailUnico);

        assertEquals(Calidad.UHD_4K, vigente.getCalidad());
    }

    @Test
    void buscarUltimaPorUsuarioSinSuscripcionesDevuelveNull() {
        // No se registra en emailsDePrueba: nunca se guarda nada, no hay nada que limpiar.
        String emailQueNuncaSeUso = "test-" + UUID.randomUUID() + "@correo.com";

        assertNull(dao.buscarUltimaPorUsuario(emailQueNuncaSeUso));
    }

    /**
     * Genera un email unico y lo registra para que @AfterEach lo borre despues.
     */
    private String emailDePrueba() {
        String email = "test-" + UUID.randomUUID() + "@correo.com";
        emailsDePrueba.add(email);
        return email;
    }
}
