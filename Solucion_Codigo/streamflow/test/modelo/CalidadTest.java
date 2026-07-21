package modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba solo lo que nosotros programamos en el enum: el precio mensual
 * asociado a cada calidad. No probamos values()/valueOf(), que son
 * generados automaticamente por Java y no forman parte de nuestra logica
 * (esos son los que el wizard de NetBeans agrega solo, y no aportan nada).
 */
class CalidadTest {

    @Test
    void sdTieneElPrecioMensualCorrecto() {
        assertEquals(5.0, Calidad.SD.getPrecioMensual());
    }

    @Test
    void hdTieneElPrecioMensualCorrecto() {
        assertEquals(8.0, Calidad.HD.getPrecioMensual());
    }

    @Test
    void uhd4kTieneElPrecioMensualCorrecto() {
        assertEquals(12.0, Calidad.UHD_4K.getPrecioMensual());
    }

    @Test
    void aMayorCalidadCorrespondeMayorPrecio() {
        assertTrue(Calidad.SD.getPrecioMensual() < Calidad.HD.getPrecioMensual());
        assertTrue(Calidad.HD.getPrecioMensual() < Calidad.UHD_4K.getPrecioMensual());
    }
}
