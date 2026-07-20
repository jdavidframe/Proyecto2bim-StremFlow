/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.ContenidoDAO;
import java.util.List;
import modelo.Contenido;

/**
 *
 * @author jesudavi
 */
public class CatalogoService {

    private final ContenidoDAO contenidoDAO;

    public CatalogoService(ContenidoDAO contenidoDAO) {
        this.contenidoDAO = contenidoDAO;
    }

    public boolean agregarContenido(Contenido contenido) {
        return contenidoDAO.guardar(contenido);
    }

    public boolean actualizarContenido(Contenido contenido) {
        return contenidoDAO.actualizar(contenido);
    }

    public boolean eliminarContenido(long id) {
        return contenidoDAO.eliminar(id);
    }

    public List<Contenido> listarCatalogo() {
        return contenidoDAO.listarTodos();
    }

    public List<Contenido> listarPorGenero(String genero) {
        return contenidoDAO.listarPorGenero(genero);
    }
}
