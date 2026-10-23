package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.pago;

public class PagoTarjeta implements MetodoPago {

    private static final double PORCENTAJE_RECARGO = 0.03;

    @Override
    public double calcularRecargo(double monto) {
        return monto * PORCENTAJE_RECARGO;
    }
}
