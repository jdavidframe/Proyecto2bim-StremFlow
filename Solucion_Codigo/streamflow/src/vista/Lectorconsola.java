/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import java.util.Scanner;
import modelo.Calidad;

/**
 *
 * @author jesudavi
 */
final class LectorConsola {

    private static final Scanner sc = new Scanner(System.in);

    private LectorConsola() {
    }

    static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero valido.");
            }
        }
    }

    static Calidad leerCalidad() {
        Calidad[] valores = Calidad.values();
        System.out.println("Calidades disponibles:");
        for (int i = 0; i < valores.length; i++) {
            System.out.printf("%d. %s ($%.2f/mes)%n", i + 1, valores[i], valores[i].getPrecioMensual());
        }

        int opcion = leerEntero("Seleccione la calidad: ");
        if (opcion < 1 || opcion > valores.length) {
            System.out.println("Opcion invalida, se asigna SD por defecto.");
            return Calidad.SD;
        }
        return valores[opcion - 1];
    }
}
