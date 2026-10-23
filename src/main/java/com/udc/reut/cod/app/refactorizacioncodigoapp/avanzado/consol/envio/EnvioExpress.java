package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio;

public class EnvioExpress implements MetodoEnvio {

    private static final double COSTO = 25_000;

    @Override
    public double calcularCosto(double montoConDescuento) {
        return COSTO;
    }
}
