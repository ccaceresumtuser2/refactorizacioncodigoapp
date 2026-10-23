package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon.Cupon;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon.SinCupon;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio.MetodoEnvio;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.PedidoInvalidoException;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.pago.MetodoPago;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad central del dominio.
 *
 *  - Patrón Builder: reemplaza el método de 7 parámetros por una construcción
 *    legible y validada.
 *  - Experto en Información (GRASP): el pedido tiene los datos, así que él
 *    mismo calcula su recibo delegando en sus estrategias.
 */
public class Pedido {

    private final Cliente cliente;
    private final List<ItemPedido> items;
    private final Cupon cupon;
    private final MetodoEnvio metodoEnvio;
    private final MetodoPago metodoPago;

    private Pedido(Builder builder) {
        this.cliente = builder.cliente;
        this.items = List.copyOf(builder.items);
        this.cupon = builder.cupon;
        this.metodoEnvio = builder.metodoEnvio;
        this.metodoPago = builder.metodoPago;
    }

    public static Builder para(Cliente cliente) {
        return new Builder(cliente);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public Recibo calcularRecibo() {
        double subtotal = calcularSubtotal();
        double descuento = cupon.calcularDescuento(subtotal);
        double montoConDescuento = subtotal - descuento;
        double costoEnvio = cupon.ajustarCostoEnvio(metodoEnvio.calcularCosto(montoConDescuento));
        double recargo = metodoPago.calcularRecargo(montoConDescuento + costoEnvio);
        return Recibo.de(subtotal, descuento, costoEnvio, recargo);
    }

    private double calcularSubtotal() {
        return items.stream()
                .mapToDouble(ItemPedido::calcularSubtotal)
                .sum();
    }

    public static class Builder {

        private final Cliente cliente;
        private final List<ItemPedido> items = new ArrayList<>();
        private Cupon cupon = new SinCupon();
        private MetodoEnvio metodoEnvio;
        private MetodoPago metodoPago;

        private Builder(Cliente cliente) {
            if (cliente == null) {
                throw new PedidoInvalidoException("El cliente es obligatorio");
            }
            this.cliente = cliente;
        }

        public Builder agregar(Producto producto, int cantidad) {
            items.add(new ItemPedido(producto, cantidad));
            return this;
        }

        public Builder conCupon(Cupon cupon) {
            this.cupon = cupon;
            return this;
        }

        public Builder enviarPor(MetodoEnvio metodoEnvio) {
            this.metodoEnvio = metodoEnvio;
            return this;
        }

        public Builder pagarCon(MetodoPago metodoPago) {
            this.metodoPago = metodoPago;
            return this;
        }

        public Pedido construir() {
            if (items.isEmpty()) {
                throw new PedidoInvalidoException("El pedido no tiene productos");
            }
            if (metodoEnvio == null) {
                throw new PedidoInvalidoException("Debe indicar el método de envío");
            }
            if (metodoPago == null) {
                throw new PedidoInvalidoException("Debe indicar el método de pago");
            }
            return new Pedido(this);
        }
    }
}
