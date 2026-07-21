/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package modelo;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica el comportamiento polimorfico de Contenido: cada subclase define su
 * propio reproducir()/obtenerDetalles(), y cualquier Contenido debe poder
 * tratarse de forma generica sin romper nada (LSP).
 */
class ContenidoPolimorfismoTest {

    @Test
    void peliculaReproduceMencionandoSuTitulo() {
        Contenido pelicula = new Pelicula("Inception", "Ciencia Ficcion", Calidad.HD, 148);

        assertTrue(pelicula.reproducir().contains("Inception"));
        assertTrue(pelicula.reproducir().toLowerCase().contains("pelicula"));
    }

    @Test
    void serieReproduceIndicandoQueEsSerie() {
        Contenido serie = new Serie("Dark", "Misterio", Calidad.UHD_4K, 3);

        assertTrue(serie.reproducir().toLowerCase().contains("serie"));
    }

    @Test
    void documentalReproduceIndicandoQueEsDocumental() {
        Contenido documental = new Documental("Cosmos", "Ciencia", Calidad.SD, "Carl Sagan");

        assertTrue(documental.reproducir().toLowerCase().contains("documental"));
    }

    /**
     * LSP: este metodo recorre una lista de Contenido sin preguntar en ningun
     * momento el tipo concreto de cada elemento (igual que hace
     * VistaAdministrador/VistaUsuario en la app real). Si alguna subclase
     * rompiera este contrato, el bucle fallaria.
     */
    @Test
    void cualquierContenidoRespondeAObtenerDetallesSinImportarElTipo() {
        List<Contenido> catalogo = List.of(
                new Pelicula("Inception", "Ciencia Ficcion", Calidad.HD, 148),
                new Serie("Dark", "Misterio", Calidad.UHD_4K, 3),
                new Documental("Cosmos", "Ciencia", Calidad.SD, "Carl Sagan")
        );

        for (Contenido contenido : catalogo) {
            assertNotNull(contenido.obtenerDetalles());
            assertFalse(contenido.obtenerDetalles().isBlank());
        }
    }

    @Test
    void obtenerDetallesDistingueElTipoDeContenidoEnElTexto() {
        Contenido pelicula = new Pelicula("Inception", "Ciencia Ficcion", Calidad.HD, 148);
        Contenido serie = new Serie("Dark", "Misterio", Calidad.UHD_4K, 3);
        Contenido documental = new Documental("Cosmos", "Ciencia", Calidad.SD, "Carl Sagan");

        assertTrue(pelicula.obtenerDetalles().contains("Pelicula"));
        assertTrue(serie.obtenerDetalles().contains("Serie"));
        assertTrue(documental.obtenerDetalles().contains("Documental"));
    }

    @Test
    void serieGuardaCorrectamenteSuNumeroDeTemporadas() {
        Serie serie = new Serie("Stranger Things", "Terror", Calidad.HD, 4);

        assertEquals(4, serie.getTemporadas());
        assertEquals(Calidad.HD, serie.getCalidadDisponible());
    }
}
