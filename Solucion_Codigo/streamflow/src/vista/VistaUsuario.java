/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import dao.ContenidoDAOImpl;
import dao.SuscripcionDAOImpl;
import java.util.List;
import modelo.Calidad;
import modelo.Contenido;
import modelo.Suscripcion;
import service.CatalogoService;
import service.RecomendacionService;
import service.SuscripcionService;

/**
 *
 * @author jesudavi
 */
public class VistaUsuario {
 
    private final CatalogoService catalogoService;
    private final RecomendacionService recomendacionService;
    private final SuscripcionService suscripcionService;
 
    public VistaUsuario() {
        this.catalogoService = new CatalogoService(new ContenidoDAOImpl());
        this.recomendacionService = new RecomendacionService(new ContenidoDAOImpl());
        this.suscripcionService = new SuscripcionService(new SuscripcionDAOImpl());
    }
 
    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = LectorConsola.leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1 -> verCatalogo();
                case 2 -> verCatalogoPorGenero();
                case 3 -> reproducirContenido();
                case 4 -> contratarSuscripcion();
                case 5 -> verSuscripcionVigente();
                case 0 -> System.out.println("Volviendo al menu principal...");
                default -> System.out.println("Opcion invalida, intente de nuevo.");
            }
            System.out.println();
        } while (opcion != 0);
    }
 
    private void mostrarMenu() {
        System.out.println("----------------------------------------");
        System.out.println("              Panel Usuario");
        System.out.println("----------------------------------------");
        System.out.println("1. Ver catalogo completo");
        System.out.println("2. Ver catalogo por genero");
        System.out.println("3. Reproducir contenido (simulado)");
        System.out.println("4. Contratar / cambiar suscripcion");
        System.out.println("5. Ver mi suscripcion vigente");
        System.out.println("0. Volver al menu principal");
    }
 
    private void verCatalogo() {
        mostrarLista(catalogoService.listarCatalogo(), "El catalogo esta vacio.");
    }
 
    private void verCatalogoPorGenero() {
        String genero = LectorConsola.leerTexto("Genero a consultar: ");
        mostrarLista(catalogoService.listarPorGenero(genero), "No hay contenido de ese genero.");
    }
 
    private void mostrarLista(List<Contenido> lista, String mensajeVacio) {
        if (lista.isEmpty()) {
            System.out.println(mensajeVacio);
            return;
        }
        for (Contenido contenido : lista) {
            System.out.println(contenido.obtenerDetalles());
        }
    }
 
    private void contratarSuscripcion() {
        System.out.println("--- Contratar Suscripcion ---");
        String email = LectorConsola.leerTexto("Su email: ");
        Calidad calidad = LectorConsola.leerCalidad();
        String fecha = LectorConsola.leerTexto("Fecha de inicio (dd/mm/aaaa): ");
 
        String error = suscripcionService.contratarSuscripcion(email, calidad, fecha);
        if (error == null) {
            double costo = suscripcionService.calcularCosto(calidad);
            System.out.printf("Suscripcion contratada. Costo mensual: $%.2f%n", costo);
        } else {
            System.out.println("Error: " + error);
        }
    }
 
    private void verSuscripcionVigente() {
        String email = LectorConsola.leerTexto("Su email: ");
        Suscripcion vigente = suscripcionService.obtenerSuscripcionVigente(email);
        if (vigente == null) {
            System.out.println("No tiene ninguna suscripcion registrada.");
            return;
        }
        System.out.println(vigente);
    }
 
    private void reproducirContenido() {
        System.out.println("--- Reproducir Contenido ---");
        List<Contenido> catalogo = catalogoService.listarCatalogo();
        if (catalogo.isEmpty()) {
            System.out.println("El catalogo esta vacio.");
            return;
        }
 
        for (int i = 0; i < catalogo.size(); i++) {
            System.out.println((i + 1) + ". " + catalogo.get(i).obtenerDetalles());
        }
 
        int opcion = LectorConsola.leerEntero("Que desea reproducir (0 para cancelar): ");
        if (opcion <= 0 || opcion > catalogo.size()) {
            System.out.println("Operacion cancelada.");
            return;
        }
 
        Contenido seleccionado = catalogo.get(opcion - 1);
 
        System.out.println(seleccionado.reproducir());
 
        mostrarRecomendacionesPara(seleccionado);
    }
 
    private void mostrarRecomendacionesPara(Contenido visto) {
        List<Contenido> recomendaciones = recomendacionService.recomendarSegunFavorito(visto);
 
        if (recomendaciones.isEmpty()) {
            System.out.println("No hay mas contenido del genero '" + visto.getGenero() + "' por ahora.");
            return;
        }
 
        System.out.println("Como viste '" + visto.getTitulo() + "', tambien te podria interesar:");
        for (Contenido recomendado : recomendaciones) {
            System.out.println(recomendado.obtenerDetalles());
        }
    }
}

