/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jesudavi
 */
public abstract class Contenido {
 
    private long id;
    private String titulo;
    private String genero;
    private Calidad calidadDisponible;
 
    protected Contenido() {
    }
 
    protected Contenido(String titulo, String genero, Calidad calidadDisponible) {
        this.titulo = titulo;
        this.genero = genero;
        this.calidadDisponible = calidadDisponible;
    }
 
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }
 
    public String getTitulo() {
        return titulo;
    }
 
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
 
    public String getGenero() {
        return genero;
    }
 
    public void setGenero(String genero) {
        this.genero = genero;
    }
 
    public Calidad getCalidadDisponible() {
        return calidadDisponible;
    }
 
    public void setCalidadDisponible(Calidad calidadDisponible) {
        this.calidadDisponible = calidadDisponible;
    }
 
    public abstract String reproducir();
 
    public abstract String obtenerDetalles();
 
    @Override
    public String toString() {
        return obtenerDetalles();
    }
}

