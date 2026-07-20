/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.SuscripcionDAO;
import java.util.List;
import modelo.Calidad;
import modelo.Suscripcion;

/**
 * @author Emily
 */

public class SuscripcionService {

    private final SuscripcionDAO suscripcionDAO;

    public SuscripcionService(SuscripcionDAO suscripcionDAO) {
        this.suscripcionDAO = suscripcionDAO;
    }

    public double calcularCosto(Calidad calidad) {//mensual 
        return calidad.getPrecioMensual();
    }

    public String contratarSuscripcion(String usuarioEmail, Calidad calidad, String fechaInicio) {
        if (usuarioEmail == null || usuarioEmail.trim().isEmpty()) {
            return "El email del usuario es obligatorio.";
        }
        if (calidad == null) {
            return "Debe seleccionar una calidad valida.";
        }
        double costo = calcularCosto(calidad);
        Suscripcion suscripcion = new Suscripcion(usuarioEmail.trim(), calidad, costo, fechaInicio);

        return suscripcionDAO.guardar(suscripcion) ? null : "No se pudo registrar la suscripcion.";
    }

    public Suscripcion obtenerSuscripcionVigente(String usuarioEmail) {
        return suscripcionDAO.buscarUltimaPorUsuario(usuarioEmail);
    }

    public List<Suscripcion> historial(String usuarioEmail) {
        return suscripcionDAO.listarPorUsuario(usuarioEmail);
    }
}

