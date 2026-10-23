package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio;

public class EnvioEstandar implements MetodoEnvio {

    private static final double COSTO = 10_000;
    private static final double MONTO_ENVIO_GRATIS = 200_000;

    @Override
    public double calcularCosto(double montoConDescuento) {
        return montoConDescuento >= MONTO_ENVIO_GRATIS ? 0 : COSTO;
    }
}
