package com.udc.reut.cod.app.refactorizacioncodigoapp.basico.consol;

/**
 * Refactorización: "Reemplazar arreglos paralelos por un objeto".
 * Precio y cantidad viajan juntos y se validan en un único lugar.
 */
public class LineaFactura {

    private final double precioUnitario;
    private final int cantidad;

    public LineaFactura(double precioUnitario, int cantidad) {
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }
}
