package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio;

/**
 * Patrón Strategy para el cálculo del costo de envío.
 */
public interface MetodoEnvio {

    double calcularCosto(double montoConDescuento);
}
