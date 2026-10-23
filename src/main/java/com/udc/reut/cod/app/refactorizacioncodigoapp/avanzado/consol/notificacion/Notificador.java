package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.notificacion;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Cliente;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Recibo;

/**
 * Patrón Observer simplificado: el procesador avisa a todos los notificadores
 * registrados sin saber si envían email, SMS, WhatsApp, etc.
 */
public interface Notificador {

    void notificar(Cliente cliente, Recibo recibo);
}
