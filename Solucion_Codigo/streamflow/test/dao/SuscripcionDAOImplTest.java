package dao;

import java.util.List;
import java.util.UUID;
import modelo.Calidad;
import modelo.Suscripcion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba de integracion real contra SQLite. Usa la MISMA base que la app
 * (db/streamflow.db) porque SuscripcionDAOImpl no permite inyectar otra URL.
 *
 * IMPORTANTE: SuscripcionDAO no tiene ningun metodo eliminar(), asi que estos
 * tests NO pueden limpiar lo que insertan. Cada test usa un email unico (UUID)
 * para no interferir con datos reales ni con otros tests, pero esos registros
 * de prueba quedaran permanentemente en tu base de datos (identificables porque
 * el email empieza con "test-").
 */
class SuscripcionDAOImplTest {

    private final SuscripcionDAO dao = new SuscripcionDAOImpl();

    @Test
    void guardarPersisteLaSuscripcion() {
        String emailUnico = "test-" + UUID.randomUUID() + "@correo.com";
        Suscripcion suscripcion = new Suscripcion(emailUnico, Calidad.HD, 8.0, "2026-07-20");

        assertTrue(dao.guardar(suscripcion));
        assertEquals(1, dao.listarPorUsuario(emailUnico).size());
    }

    @Test
    void listarPorUsuarioDevuelveSoloLasDeEseUsuario() {
        String emailA = "test-" + UUID.randomUUID() + "@correo.com";
        String emailB = "test-" + UUID.randomUUID() + "@correo.com";
        dao.guardar(new Suscripcion(emailA, Calidad.HD, 8.0, "2026-07-20"));
        dao.guardar(new Suscripcion(emailB, Calidad.SD, 5.0, "2026-07-20"));

        List<Suscripcion> deA = dao.listarPorUsuario(emailA);

        assertEquals(1, deA.size());
        assertEquals(emailA, deA.get(0).getUsuarioEmail());
    }

    @Test
    void buscarUltimaPorUsuarioDevuelveLaMasReciente() {
        String emailUnico = "test-" + UUID.randomUUID() + "@correo.com";
        dao.guardar(new Suscripcion(emailUnico, Calidad.SD, 5.0, "2026-01-01"));
        dao.guardar(new Suscripcion(emailUnico, Calidad.UHD_4K, 12.0, "2026-07-20"));

        Suscripcion vigente = dao.buscarUltimaPorUsuario(emailUnico);

        assertEquals(Calidad.UHD_4K, vigente.getCalidad());
    }

    @Test
    void buscarUltimaPorUsuarioSinSuscripcionesDevuelveNull() {
        String emailQueNuncaSeUso = "test-" + UUID.randomUUID() + "@correo.com";

        assertNull(dao.buscarUltimaPorUsuario(emailQueNuncaSeUso));
    }
}
