/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author jesudavi
 */
public class VistaConsola {
 
    private final VistaAdministrador panelAdmin;
    private final VistaUsuario panelUsuario;
 
    public VistaConsola() {
        this.panelAdmin = new VistaAdministrador();
        this.panelUsuario = new VistaUsuario();
    }
 
    public void iniciar() {
        int opcion;
        do {
            System.out.println("========================================");
            System.out.println("     StreamFlow - Gestion de Contenido");
            System.out.println("========================================");
            System.out.println("1. Panel Administrador");
            System.out.println("2. Panel Usuario");
            System.out.println("0. Salir");
 
            opcion = LectorConsola.leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1 -> panelAdmin.iniciar();
                case 2 -> panelUsuario.iniciar();
                case 0 -> System.out.println("Saliendo de StreamFlow...");
                default -> System.out.println("Opcion invalida, intente de nuevo.");
            }
            System.out.println();
        } while (opcion != 0);
    }
}

