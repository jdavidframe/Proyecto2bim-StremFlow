/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import dao.ContenidoDAOImpl;
import java.util.List;
import modelo.Calidad;
import modelo.Contenido;
import modelo.Documental;
import modelo.Pelicula;
import modelo.Serie;
import service.CatalogoService;

/**
 *
 * @author jesudavi
 */
public class VistaAdministrador {

    private final CatalogoService catalogoService;

    public VistaAdministrador() {
        this.catalogoService = new CatalogoService(new ContenidoDAOImpl());
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = LectorConsola.leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1 -> agregarPelicula();
                case 2 -> agregarSerie();
                case 3 -> agregarDocumental();
                case 4 -> listarCatalogo();
                case 5 -> eliminarContenido();
                case 0 -> System.out.println("Volviendo al menu principal...");
                default -> System.out.println("Opcion invalida, intente de nuevo.");
            }
            System.out.println();
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("----------------------------------------");
        System.out.println("           Panel Administrador");
        System.out.println("----------------------------------------");
        System.out.println("1. Agregar pelicula");
        System.out.println("2. Agregar serie");
        System.out.println("3. Agregar documental");
        System.out.println("4. Listar catalogo completo");
        System.out.println("5. Eliminar contenido");
        System.out.println("0. Volver al menu principal");
    }

    private void agregarPelicula() {
        System.out.println("--- Agregar Pelicula ---");
        String titulo = LectorConsola.leerTexto("Titulo: ");
        String genero = LectorConsola.leerTexto("Genero: ");
        Calidad calidad = LectorConsola.leerCalidad();
        int duracion = LectorConsola.leerEntero("Duracion (minutos): ");

        guardar(new Pelicula(titulo, genero, calidad, duracion));
    }

    private void agregarSerie() {
        System.out.println("--- Agregar Serie ---");
        String titulo = LectorConsola.leerTexto("Titulo: ");
        String genero = LectorConsola.leerTexto("Genero: ");
        Calidad calidad = LectorConsola.leerCalidad();
        int temporadas = LectorConsola.leerEntero("Numero de temporadas: ");

        guardar(new Serie(titulo, genero, calidad, temporadas));
    }

    private void agregarDocumental() {
        System.out.println("--- Agregar Documental ---");
        String titulo = LectorConsola.leerTexto("Titulo: ");
        String genero = LectorConsola.leerTexto("Genero: ");
        Calidad calidad = LectorConsola.leerCalidad();
        String director = LectorConsola.leerTexto("Director: ");

        guardar(new Documental(titulo, genero, calidad, director));
    }

    private void guardar(Contenido contenido) {
        boolean ok = catalogoService.agregarContenido(contenido);
        System.out.println(ok ? "Contenido agregado exitosamente." : "No se pudo agregar el contenido.");
    }

    private void listarCatalogo() {
        List<Contenido> catalogo = catalogoService.listarCatalogo();
        if (catalogo.isEmpty()) {
            System.out.println("El catalogo esta vacio.");
            return;
        }

        for (Contenido contenido : catalogo) {
            System.out.println(contenido.obtenerDetalles());
        }
    }

    private void eliminarContenido() {
        List<Contenido> catalogo = catalogoService.listarCatalogo();
        if (catalogo.isEmpty()) {
            System.out.println("El catalogo esta vacio.");
            return;
        }

        for (int i = 0; i < catalogo.size(); i++) {
            System.out.println((i + 1) + ". " + catalogo.get(i).obtenerDetalles());
        }

        int opcion = LectorConsola.leerEntero("Seleccione el contenido a eliminar (0 para cancelar): ");
        if (opcion <= 0 || opcion > catalogo.size()) {
            System.out.println("Operacion cancelada.");
            return;
        }

        Contenido seleccionado = catalogo.get(opcion - 1);
        boolean ok = catalogoService.eliminarContenido(seleccionado.getId());
        System.out.println(ok ? "Contenido eliminado." : "No se pudo eliminar el contenido.");
    }
}
