package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon;

/**
 * Patrón Null Object: evita preguntar "if (cupon != null)" en todo el código.
 */
public class SinCupon implements Cupon {

    @Override
    public double calcularDescuento(double subtotal) {
        return 0;
    }
}
