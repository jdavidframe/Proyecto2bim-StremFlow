package service;

import dao.ContenidoDAO;
import java.util.ArrayList;
import java.util.List;
import modelo.Calidad;
import modelo.Contenido;
import modelo.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba CatalogoService de forma aislada, sin tocar SQLite: se le inyecta
 * un ContenidoDAO falso en memoria y solo verificamos que delega bien.
 */
class CatalogoServiceTest {

    private ContenidoDAOEnMemoria daoFalso;
    private CatalogoService service;

    @BeforeEach
    void setUp() {
        daoFalso = new ContenidoDAOEnMemoria();
        service = new CatalogoService(daoFalso);
    }

    @Test
    void agregarContenidoDelegaEnGuardar() {
        Contenido pelicula = new Pelicula("Inception", "Ciencia Ficcion", Calidad.HD, 148);

        assertTrue(service.agregarContenido(pelicula));
        assertTrue(daoFalso.guardados.contains(pelicula));
    }

    @Test
    void eliminarContenidoDelegaEnEliminarConElIdCorrecto() {
        assertTrue(service.eliminarContenido(7L));
        assertEquals(7L, daoFalso.idEliminado);
    }

    @Test
    void listarCatalogoDevuelveTodoLoQueTieneElDao() {
        daoFalso.guardados.add(new Pelicula("Inception", "Ciencia Ficcion", Calidad.HD, 148));

        assertEquals(1, service.listarCatalogo().size());
    }

    @Test
    void listarPorGeneroDelegaEnElDaoConElGeneroCorrecto() {
        service.listarPorGenero("Terror");

        assertEquals("Terror", daoFalso.generoConsultado);
    }

    /** Fake simple en memoria: evita depender de SQLite para probar el service. */
    private static class ContenidoDAOEnMemoria implements ContenidoDAO {
        List<Contenido> guardados = new ArrayList<>();
        long idEliminado = -1;
        String generoConsultado;

        @Override
        public boolean guardar(Contenido contenido) {
            guardados.add(contenido);
            return true;
        }

        @Override
        public boolean actualizar(Contenido contenido) {
            return true;
        }

        @Override
        public boolean eliminar(long id) {
            idEliminado = id;
            return true;
        }

        @Override
        public List<Contenido> listarTodos() {
            return guardados;
        }

        @Override
        public List<Contenido> listarPorGenero(String genero) {
            generoConsultado = genero;
            return guardados;
        }
    }
}
