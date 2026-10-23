package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio;

public class EnvioInternacional implements MetodoEnvio {

    private static final double COSTO = 50_000;

    @Override
    public double calcularCosto(double montoConDescuento) {
        return COSTO;
    }
}
