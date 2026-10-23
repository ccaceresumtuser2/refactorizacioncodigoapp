package com.udc.reut.cod.app.refactorizacioncodigoapp.basico.sinsol;

/**
 * EJEMPLO 1 - NIVEL BÁSICO (SIN REFACTORIZAR)
 *
 * Calcula el total de una factura: suma de productos, descuento según el
 * tipo de cliente e IVA.
 *
 * Code smells presentes:
 *  - Nombres poco expresivos (calc, p, c, t, s, d, r).
 *  - Números mágicos (0.1, 0.05, 0.19, 100.0).
 *  - Método largo que hace varias cosas a la vez.
 *  - Arreglos paralelos (precios[] y cantidades[]) que deben coincidir.
 *  - Tipo de cliente como String ("obsesión por primitivos").
 */
public class Factura {

    public double calc(double[] p, int[] c, String t) {
        double s = 0;
        for (int i = 0; i < p.length; i++) {
            s = s + p[i] * c[i];
        }
        double d = 0;
        if (t.equals("VIP")) {
            d = s * 0.1;
        } else if (t.equals("FRECUENTE")) {
            d = s * 0.05;
        }
        double r = s - d;
        r = r + r * 0.19;
        return Math.round(r * 100.0) / 100.0;
    }
}
