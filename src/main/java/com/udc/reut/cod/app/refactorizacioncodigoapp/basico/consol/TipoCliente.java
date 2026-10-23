package com.udc.reut.cod.app.refactorizacioncodigoapp.basico.consol;

/**
 * Refactorización: "Reemplazar String por Enum".
 * Cada tipo de cliente conoce su propio porcentaje de descuento,
 * eliminando los números mágicos y los if/else de comparación de textos.
 */
public enum TipoCliente {
    REGULAR(0.0),
    FRECUENTE(0.05),
    VIP(0.10);

    private final double porcentajeDescuento;

    TipoCliente(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double calcularDescuento(double monto) {
        return monto * porcentajeDescuento;
    }
}
