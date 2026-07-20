package dao;

import java.util.List;
import modelo.Suscripcion;

/**
 * @author Emily
 * persistencia para suscripciones. 
 */
public interface SuscripcionDAO {
    
    boolean guardar(Suscripcion suscripcion);
    List<Suscripcion> listarPorUsuario(String usuarioEmail);
    List<Suscripcion> listarTodas();
    Suscripcion buscarUltimaPorUsuario(String usuarioEmail);
    
}
