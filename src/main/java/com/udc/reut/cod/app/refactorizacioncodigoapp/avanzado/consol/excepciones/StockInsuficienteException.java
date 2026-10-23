package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones;

public class StockInsuficienteException extends PedidoInvalidoException {

    public StockInsuficienteException(String codigoProducto, int solicitado, int disponible) {
        super("Stock insuficiente para " + codigoProducto
                + ": solicitado " + solicitado + ", disponible " + disponible);
    }
}
