package dao;

import java.util.List;
import modelo.Contenido;

/**
 * @author Emily
 Los servicios dependen de esta interfaz
 */
public interface ContenidoDAO {

    boolean guardar(Contenido contenido);
    boolean actualizar(Contenido contenido);
    boolean eliminar(long id);
    List<Contenido> listarTodos();
    List<Contenido> listarPorGenero(String genero);
    
}