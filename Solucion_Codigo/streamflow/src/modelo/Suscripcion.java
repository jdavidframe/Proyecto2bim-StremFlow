/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jesudavi
 */
public class Suscripcion {

    private long id;
    private String usuarioEmail;
    private Calidad calidad;
    private double costoMensual;
    private String fechaInicio;

    public Suscripcion() {
    }

    public Suscripcion(String usuarioEmail, Calidad calidad, double costoMensual, String fechaInicio) {
        this.usuarioEmail = usuarioEmail;
        this.calidad = calidad;
        this.costoMensual = costoMensual;
        this.fechaInicio = fechaInicio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public Calidad getCalidad() {
        return calidad;
    }

    public void setCalidad(Calidad calidad) {
        this.calidad = calidad;
    }

    public double getCostoMensual() {
        return costoMensual;
    }

    public void setCostoMensual(double costoMensual) {
        this.costoMensual = costoMensual;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @Override
    public String toString() {
        return String.format("Suscripcion de %-25s | Calidad: %-6s | Costo: $%.2f/mes | Desde: %s",
                usuarioEmail, calidad, costoMensual, fechaInicio);
    }
}
