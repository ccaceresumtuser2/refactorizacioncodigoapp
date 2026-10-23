package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.PedidoInvalidoException;

/**
 * Objeto de valor inmutable (record) que reemplaza el mapa global de precios.
 */
public record Producto(String codigo, String nombre, double precio) {

    public Producto {
        if (codigo == null || codigo.isBlank()) {
            throw new PedidoInvalidoException("El código del producto es obligatorio");
        }
        if (precio < 0) {
            throw new PedidoInvalidoException("El precio no puede ser negativo");
        }
    }
}
