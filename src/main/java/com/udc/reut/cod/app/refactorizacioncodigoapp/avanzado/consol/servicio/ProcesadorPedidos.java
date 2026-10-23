package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.servicio;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.StockInsuficienteException;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.inventario.Inventario;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.ItemPedido;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Pedido;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Recibo;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.notificacion.Notificador;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * EJEMPLO 3 - NIVEL AVANZADO (REFACTORIZADO)
 *
 * Refactorizaciones y principios aplicados:
 *  - SRP: el procesador solo ORQUESTA; el cálculo vive en Pedido y sus
 *    estrategias, el stock en Inventario y los avisos en Notificador.
 *  - OCP + Strategy: cupones, envíos y pagos son clases intercambiables.
 *  - DIP + Inyección de dependencias por constructor: se reciben
 *    interfaces, por eso en las pruebas se pueden usar dobles (fakes).
 *  - Se elimina el estado global estático.
 *  - Corrección de bug: el código original validaba el stock línea por
 *    línea, así que el mismo producto repetido podía dejar stock negativo.
 *    Aquí las cantidades se agrupan por producto antes de validar.
 */
public class ProcesadorPedidos {

    private final Inventario inventario;
    private final List<Notificador> notificadores;

    public ProcesadorPedidos(Inventario inventario, List<Notificador> notificadores) {
        this.inventario = inventario;
        this.notificadores = List.copyOf(notificadores);
    }

    public Recibo procesar(Pedido pedido) {
        Map<String, Integer> cantidadesPorProducto = agruparCantidades(pedido);
        verificarStock(cantidadesPorProducto);
        Recibo recibo = pedido.calcularRecibo();
        descontarInventario(cantidadesPorProducto);
        notificar(pedido, recibo);
        return recibo;
    }

    private Map<String, Integer> agruparCantidades(Pedido pedido) {
        return pedido.getItems().stream()
                .collect(Collectors.groupingBy(
                        item -> item.producto().codigo(),
                        Collectors.summingInt(ItemPedido::cantidad)));
    }

    private void verificarStock(Map<String, Integer> cantidadesPorProducto) {
        cantidadesPorProducto.forEach((codigo, cantidad) -> {
            int disponible = inventario.consultarStock(codigo);
            if (disponible < cantidad) {
                throw new StockInsuficienteException(codigo, cantidad, disponible);
            }
        });
    }

    private void descontarInventario(Map<String, Integer> cantidadesPorProducto) {
        cantidadesPorProducto.forEach(inventario::descontar);
    }

    private void notificar(Pedido pedido, Recibo recibo) {
        notificadores.forEach(notificador -> notificador.notificar(pedido.getCliente(), recibo));
    }
}
