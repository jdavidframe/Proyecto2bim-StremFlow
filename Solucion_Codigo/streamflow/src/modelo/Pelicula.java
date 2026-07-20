/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jesudavi
 */
public class Pelicula extends Contenido {

    private int duracionMinutos;

    public Pelicula() {
        super();
    }

    public Pelicula(String titulo, String genero, Calidad calidadDisponible, int duracionMinutos) {
        super(titulo, genero, calidadDisponible);
        this.duracionMinutos = duracionMinutos;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    @Override
    public String reproducir() {
        return "Reproduciendo pelicula '" + getTitulo() + "' en calidad " + getCalidadDisponible();
    }

    @Override
    public String obtenerDetalles() {
        return String.format("[Pelicula] %-25s | Genero: %-12s | Duracion: %3d min | Calidad: %s",
                getTitulo(), getGenero(), duracionMinutos, getCalidadDisponible());
    }
}
