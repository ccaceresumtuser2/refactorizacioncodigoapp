package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.PedidoInvalidoException;

/**
 * Reemplaza los arreglos paralelos codigos[] / cantidades[].
 */
public record ItemPedido(Producto producto, int cantidad) {

    public ItemPedido {
        if (producto == null) {
            throw new PedidoInvalidoException("El producto es obligatorio");
        }
        if (cantidad <= 0) {
            throw new PedidoInvalidoException("Cantidad inválida para " + producto.codigo());
        }
    }

    public double calcularSubtotal() {
        return producto.precio() * cantidad;
    }
}
