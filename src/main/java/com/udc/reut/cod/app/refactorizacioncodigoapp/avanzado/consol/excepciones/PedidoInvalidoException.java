package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones;

/**
 * Refactorización: "Reemplazar excepciones genéricas por excepciones de dominio".
 */
public class PedidoInvalidoException extends RuntimeException {

    public PedidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
