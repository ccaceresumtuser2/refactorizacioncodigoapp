package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.notificacion;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Cliente;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Recibo;

public class NotificadorSms implements Notificador {

    @Override
    public void notificar(Cliente cliente, Recibo recibo) {
        if (cliente.tieneTelefono()) {
            System.out.println("SMS a " + cliente.telefono()
                    + ": Su pedido por $" + recibo.total() + " fue confirmado");
        }
    }
}
