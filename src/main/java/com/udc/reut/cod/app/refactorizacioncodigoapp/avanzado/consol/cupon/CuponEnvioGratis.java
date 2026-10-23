package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon;

public class CuponEnvioGratis implements Cupon {

    @Override
    public double calcularDescuento(double subtotal) {
        return 0;
    }

    @Override
    public double ajustarCostoEnvio(double costoEnvio) {
        return 0;
    }
}
