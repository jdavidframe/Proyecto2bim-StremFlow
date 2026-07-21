package service;

import dao.SuscripcionDAO;
import java.util.ArrayList;
import java.util.List;
import modelo.Calidad;
import modelo.Suscripcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba SuscripcionService de forma aislada, sin tocar SQLite: se le
 * inyecta un SuscripcionDAO falso en memoria (DIP - el service solo conoce
 * la interfaz, nunca la implementacion concreta).
 */
class SuscripcionServiceTest {

    private SuscripcionDAOEnMemoria daoFalso;
    private SuscripcionService service;

    @BeforeEach
    void setUp() {
        daoFalso = new SuscripcionDAOEnMemoria();
        service = new SuscripcionService(daoFalso);
    }

    @Test
    void calcularCostoDevuelveElPrecioDeLaCalidad() {
        assertEquals(5.0, service.calcularCosto(Calidad.SD));
        assertEquals(8.0, service.calcularCosto(Calidad.HD));
        assertEquals(12.0, service.calcularCosto(Calidad.UHD_4K));
    }

    @Test
    void contratarSuscripcionConDatosValidosNoDevuelveError() {
        String error = service.contratarSuscripcion("ana@correo.com", Calidad.HD, "2026-07-20");

        assertNull(error);
        assertEquals(1, daoFalso.guardadas.size());
        assertEquals(8.0, daoFalso.guardadas.get(0).getCostoMensual());
    }

    @Test
    void contratarSuscripcionSinEmailDevuelveMensajeDeErrorYNoGuardaNada() {
        String error = service.contratarSuscripcion("   ", Calidad.HD, "2026-07-20");

        assertNotNull(error);
        assertTrue(daoFalso.guardadas.isEmpty());
    }

    @Test
    void contratarSuscripcionSinCalidadDevuelveMensajeDeErrorYNoGuardaNada() {
        String error = service.contratarSuscripcion("ana@correo.com", null, "2026-07-20");

        assertNotNull(error);
        assertTrue(daoFalso.guardadas.isEmpty());
    }

    @Test
    void obtenerSuscripcionVigenteDelegaEnElDao() {
        Suscripcion esperada = new Suscripcion("ana@correo.com", Calidad.SD, 5.0, "2026-01-01");
        daoFalso.ultima = esperada;

        assertSame(esperada, service.obtenerSuscripcionVigente("ana@correo.com"));
    }

    @Test
    void historialDelegaEnElDao() {
        daoFalso.guardar(new Suscripcion("ana@correo.com", Calidad.SD, 5.0, "2026-01-01"));

        List<Suscripcion> historial = service.historial("ana@correo.com");

        assertEquals(1, historial.size());
    }

    /** Fake simple en memoria: evita depender de SQLite para probar el service. */
    private static class SuscripcionDAOEnMemoria implements SuscripcionDAO {
        List<Suscripcion> guardadas = new ArrayList<>();
        Suscripcion ultima;

        @Override
        public boolean guardar(Suscripcion suscripcion) {
            guardadas.add(suscripcion);
            return true;
        }

        @Override
        public List<Suscripcion> listarPorUsuario(String usuarioEmail) {
            return guardadas;
        }

        @Override
        public List<Suscripcion> listarTodas() {
            return guardadas;
        }

        @Override
        public Suscripcion buscarUltimaPorUsuario(String usuarioEmail) {
            return ultima;
        }
    }
}
