package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.notificacion;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Cliente;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Recibo;

public class NotificadorEmail implements Notificador {

    @Override
    public void notificar(Cliente cliente, Recibo recibo) {
        if (cliente.tieneEmail()) {
            System.out.println("EMAIL a " + cliente.email()
                    + ": Su pedido por $" + recibo.total() + " fue confirmado");
        }
    }
}
