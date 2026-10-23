package com.udc.reut.cod.app.refactorizacioncodigoapp.basico.consol;

import java.util.ArrayList;
import java.util.List;

/**
 * EJEMPLO 1 - NIVEL BÁSICO (REFACTORIZADO)
 *
 * Refactorizaciones aplicadas:
 *  - Renombrar variables y métodos con nombres expresivos.
 *  - Reemplazar números mágicos por constantes.
 *  - Extraer método: cada paso del cálculo es un método pequeño.
 *  - Encapsulamiento: la factura administra su propia lista de líneas.
 *  - Enum TipoCliente en lugar de String.
 */
public class Factura {

    private static final double TASA_IVA = 0.19;

    private final TipoCliente tipoCliente;
    private final List<LineaFactura> lineas = new ArrayList<>();

    public Factura(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public void agregarLinea(double precioUnitario, int cantidad) {
        lineas.add(new LineaFactura(precioUnitario, cantidad));
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (LineaFactura linea : lineas) {
            subtotal += linea.calcularSubtotal();
        }
        return subtotal;
    }

    public double calcularDescuento() {
        return tipoCliente.calcularDescuento(calcularSubtotal());
    }

    public double calcularIva() {
        return (calcularSubtotal() - calcularDescuento()) * TASA_IVA;
    }

    public double calcularTotal() {
        double total = calcularSubtotal() - calcularDescuento() + calcularIva();
        return redondearADosDecimales(total);
    }

    private static double redondearADosDecimales(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
