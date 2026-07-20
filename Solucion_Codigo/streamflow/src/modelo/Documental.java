/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jesudavi
 */
public class Documental extends Contenido {

    private String director;

    public Documental() {
        super();
    }

    public Documental(String titulo, String genero, Calidad calidadDisponible, String director) {
        super(titulo, genero, calidadDisponible);
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String reproducir() {
        return "Reproduciendo documental '" + getTitulo() + "' en calidad " + getCalidadDisponible();
    }

    @Override
    public String obtenerDetalles() {
        return String.format("[Documental] %-23s | Genero: %-12s | Director: %-15s | Calidad: %s",
                getTitulo(), getGenero(), director, getCalidadDisponible());
    }
}
