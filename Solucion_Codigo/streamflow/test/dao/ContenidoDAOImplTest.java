package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import modelo.Calidad;
import modelo.Contenido;
import modelo.Documental;
import modelo.Pelicula;
import modelo.Serie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba de integracion real contra SQLite. Usa la MISMA base que la app
 * (db/streamflow.db) porque ContenidoDAOImpl no permite inyectar otra URL. Para
 * no chocar con datos reales del catalogo, cada test usa un genero unico (UUID)
 * y se limpia solo en @AfterEach usando eliminar().
 */
class ContenidoDAOImplTest {

    private final ContenidoDAO dao = new ContenidoDAOImpl();
    private final List<Long> idsCreados = new ArrayList<>();

    @AfterEach
    void tearDown() {
        for (Long id : idsCreados) {
            dao.eliminar(id);
        }
        idsCreados.clear();
    }

    @Test
    void guardarYListarPorGeneroDevuelveElContenidoGuardado() {
        String generoUnico = "TestGenero-" + UUID.randomUUID();
        Contenido pelicula = new Pelicula("Inception", generoUnico, Calidad.HD, 148);

        assertTrue(dao.guardar(pelicula));

        List<Contenido> filtrado = dao.listarPorGenero(generoUnico);
        assertEquals(1, filtrado.size());
        assertEquals("Inception", filtrado.get(0).getTitulo());

        idsCreados.add(filtrado.get(0).getId());
    }

    @Test
    void listarPorGeneroReconstruyeCadaSubtipoConcretoSegunElTipoGuardado() {
        String generoUnico = "TestGenero-" + UUID.randomUUID();
        dao.guardar(new Pelicula("Inception", generoUnico, Calidad.HD, 148));
        dao.guardar(new Serie("Dark", generoUnico, Calidad.UHD_4K, 3));
        dao.guardar(new Documental("Cosmos", generoUnico, Calidad.SD, "Carl Sagan"));

        List<Contenido> filtrado = dao.listarPorGenero(generoUnico);

        assertEquals(3, filtrado.size());
        assertTrue(filtrado.stream().anyMatch(c -> c instanceof Pelicula));
        assertTrue(filtrado.stream().anyMatch(c -> c instanceof Serie));
        assertTrue(filtrado.stream().anyMatch(c -> c instanceof Documental));

        for (Contenido c : filtrado) {
            idsCreados.add(c.getId());
        }
    }

    @Test
    void actualizarModificaLosDatosDelRegistroExistente() {
        String generoUnico = "TestGenero-" + UUID.randomUUID();
        dao.guardar(new Pelicula("Inception", generoUnico, Calidad.HD, 148));
        long id = dao.listarPorGenero(generoUnico).get(0).getId();
        idsCreados.add(id);

        Pelicula actualizada = new Pelicula("Inception (Extendida)", generoUnico, Calidad.UHD_4K, 160);
        actualizada.setId(id);

        assertTrue(dao.actualizar(actualizada));

        Contenido recargada = dao.listarPorGenero(generoUnico).get(0);
        assertEquals("Inception (Extendida)", recargada.getTitulo());
        assertEquals(Calidad.UHD_4K, recargada.getCalidadDisponible());
    }

    @Test
    void eliminarQuitaElRegistroDeLaBaseDeDatos() {
        String generoUnico = "TestGenero-" + UUID.randomUUID();
        dao.guardar(new Pelicula("Inception", generoUnico, Calidad.HD, 148));
        long id = dao.listarPorGenero(generoUnico).get(0).getId();

        assertTrue(dao.eliminar(id));
        assertTrue(dao.listarPorGenero(generoUnico).isEmpty());
    }

    @Test
    void eliminarUnIdInexistenteDevuelveFalse() {
        assertFalse(dao.eliminar(-999999L));
    }
}
