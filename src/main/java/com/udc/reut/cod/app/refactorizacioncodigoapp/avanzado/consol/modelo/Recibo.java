package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo;

/**
 * Resultado del procesamiento: en vez de devolver solo un double,
 * se devuelve el desglose completo (más fácil de mostrar y de probar).
 */
public record Recibo(double subtotal, double descuento, double costoEnvio, double recargo, double total) {

    public static Recibo de(double subtotal, double descuento, double costoEnvio, double recargo) {
        double total = subtotal - descuento + costoEnvio + recargo;
        return new Recibo(subtotal, descuento, costoEnvio, recargo, redondear(total));
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
