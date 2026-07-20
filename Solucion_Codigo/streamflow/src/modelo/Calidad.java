/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jesudavi
 */
public enum Calidad {

    SD(5.0),
    HD(8.0),
    UHD_4K(12.0);

    private final double precioMensual;

    Calidad(double precioMensual) {
        this.precioMensual = precioMensual;
    }

    public double getPrecioMensual() {
        return precioMensual;
    }
}
