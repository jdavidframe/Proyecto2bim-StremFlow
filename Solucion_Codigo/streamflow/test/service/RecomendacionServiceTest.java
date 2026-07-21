package service;

import dao.ContenidoDAO;
import java.util.ArrayList;
import java.util.List;
import modelo.Calidad;
import modelo.Contenido;
import modelo.Pelicula;
import modelo.Serie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba RecomendacionService de forma aislada, sin tocar SQLite: se le
 * inyecta un ContenidoDAO falso en memoria.
 */
class RecomendacionServiceTest {

    private ContenidoDAOEnMemoria daoFalso;
    private RecomendacionService service;

    @BeforeEach
    void setUp() {
        daoFalso = new ContenidoDAOEnMemoria();
        service = new RecomendacionService(daoFalso);
    }

    @Test
    void recomendarPorGeneroDelegaEnElDao() {
        daoFalso.porGenero.add(new Pelicula("Inception", "Ciencia Ficcion", Calidad.HD, 148));

        List<Contenido> resultado = service.recomendarPorGenero("Ciencia Ficcion");

        assertEquals(1, resultado.size());
        assertEquals("Ciencia Ficcion", daoFalso.generoConsultado);
    }

    @Test
    void recomendarSegunFavoritoExcluyeAlPropioFavorito() {
        Contenido favorito = new Pelicula("Inception", "Ciencia Ficcion", Calidad.HD, 148);
        daoFalso.porGenero.add(favorito);
        daoFalso.porGenero.add(new Pelicula("Interstellar", "Ciencia Ficcion", Calidad.HD, 169));

        List<Contenido> recomendaciones = service.recomendarSegunFavorito(favorito);

        assertEquals(1, recomendaciones.size());
        assertEquals("Interstellar", recomendaciones.get(0).getTitulo());
    }

    @Test
    void recomendarSegunFavoritoComparaTitulosSinImportarMayusculas() {
        Contenido favorito = new Pelicula("Inception", "Ciencia Ficcion", Calidad.HD, 148);
        daoFalso.porGenero.add(new Pelicula("INCEPTION", "Ciencia Ficcion", Calidad.HD, 148));

        List<Contenido> recomendaciones = service.recomendarSegunFavorito(favorito);

        assertTrue(recomendaciones.isEmpty());
    }

    @Test
    void recomendarSegunFavoritoConsultaElGeneroDelFavorito() {
        Contenido favorito = new Serie("Dark", "Misterio", Calidad.UHD_4K, 3);

        service.recomendarSegunFavorito(favorito);

        assertEquals("Misterio", daoFalso.generoConsultado);
    }

    /** Fake simple en memoria: evita depender de SQLite para probar el service. */
    private static class ContenidoDAOEnMemoria implements ContenidoDAO {
        List<Contenido> porGenero = new ArrayList<>();
        String generoConsultado;

        @Override
        public boolean guardar(Contenido contenido) {
            return true;
        }

        @Override
        public boolean actualizar(Contenido contenido) {
            return true;
        }

        @Override
        public boolean eliminar(long id) {
            return true;
        }

        @Override
        public List<Contenido> listarTodos() {
            return porGenero;
        }

        @Override
        public List<Contenido> listarPorGenero(String genero) {
            generoConsultado = genero;
            return porGenero;
        }
    }
}
