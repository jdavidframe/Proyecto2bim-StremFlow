/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jesudavi
 */
public class Serie extends Contenido {

    private int temporadas;

    public Serie() {
        super();
    }

    public Serie(String titulo, String genero, Calidad calidadDisponible, int temporadas) {
        super(titulo, genero, calidadDisponible);
        this.temporadas = temporadas;
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }

    @Override
    public String reproducir() {
        return "Reproduciendo serie '" + getTitulo() + "' - Temporada 1 en calidad " + getCalidadDisponible();
    }

    @Override
    public String obtenerDetalles() {
        return String.format("[Serie]    %-25s | Genero: %-12s | Temporadas: %3d | Calidad: %s",
                getTitulo(), getGenero(), temporadas, getCalidadDisponible());
    }
}
