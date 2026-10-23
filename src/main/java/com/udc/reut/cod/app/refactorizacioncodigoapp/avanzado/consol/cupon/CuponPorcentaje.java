package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon;

public class CuponPorcentaje implements Cupon {

    private final double porcentaje;
    private final double montoMinimo;

    public CuponPorcentaje(double porcentaje) {
        this(porcentaje, 0);
    }

    public CuponPorcentaje(double porcentaje, double montoMinimo) {
        if (porcentaje < 0 || porcentaje > 1) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 1");
        }
        this.porcentaje = porcentaje;
        this.montoMinimo = montoMinimo;
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal >= montoMinimo ? subtotal * porcentaje : 0;
    }
}
