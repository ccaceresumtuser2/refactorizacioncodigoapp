package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon;

/**
 * Patrón Strategy: cada cupón encapsula su propia regla de descuento.
 * Agregar un cupón nuevo = crear una clase nueva, sin tocar el procesador.
 */
public interface Cupon {

    double calcularDescuento(double subtotal);

    /** Por defecto un cupón no modifica el costo de envío. */
    default double ajustarCostoEnvio(double costoEnvio) {
        return costoEnvio;
    }
}
