/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.ContenidoDAO;
import java.util.ArrayList;
import java.util.List;
import modelo.Contenido;
/**
 * @author Emily 
 */

public class RecomendacionService {

    private final ContenidoDAO contenidoDAO;

    public RecomendacionService(ContenidoDAO contenidoDAO) {
        this.contenidoDAO = contenidoDAO;
    }

    public List<Contenido> recomendarPorGenero(String genero) {
        return contenidoDAO.listarPorGenero(genero);
    }

    public List<Contenido> recomendarSegunFavorito(Contenido favorito) {
        List<Contenido> mismoGenero = contenidoDAO.listarPorGenero(favorito.getGenero());
        List<Contenido> recomendaciones = new ArrayList<>();
        for (Contenido candidato : mismoGenero) {
            if (!candidato.getTitulo().equalsIgnoreCase(favorito.getTitulo())) {
                recomendaciones.add(candidato);
            }
        }
        return recomendaciones;
    }
}
