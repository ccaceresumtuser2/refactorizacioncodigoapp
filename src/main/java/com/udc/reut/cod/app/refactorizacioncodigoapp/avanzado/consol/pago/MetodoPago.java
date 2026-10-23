package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.pago;

/**
 * Patrón Strategy para el recargo según el medio de pago.
 */
public interface MetodoPago {

    double calcularRecargo(double monto);
}
