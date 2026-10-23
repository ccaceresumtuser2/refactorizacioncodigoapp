package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon.CatalogoCupones;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon.Cupon;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio.EnvioEstandar;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio.EnvioExpress;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio.EnvioInternacional;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio.MetodoEnvio;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.PedidoInvalidoException;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.StockInsuficienteException;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.inventario.InventarioEnMemoria;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Cliente;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Pedido;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Producto;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Recibo;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.pago.PagoEfectivo;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.pago.PagoTarjeta;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.servicio.ProcesadorPedidos;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Gracias a la inyección de dependencias:
 *  - Cada prueba crea su propio inventario (sin estado global).
 *  - Las notificaciones se verifican con un doble de prueba (fake),
 *    sin capturar System.out.
 */
class ProcesadorPedidosTest {

    private final Producto laptop = new Producto("P1", "Laptop", 150_000);
    private final Producto mouse = new Producto("P2", "Mouse", 20_000);
    private final Cliente cliente = new Cliente("Ana", "ana@mail.com", "3001234567");
    private final CatalogoCupones cupones = CatalogoCupones.porDefecto();
    private final List<String> notificacionesEnviadas = new ArrayList<>();

    private InventarioEnMemoria inventario;
    private ProcesadorPedidos procesador;

    @BeforeEach
    void configurar() {
        inventario = new InventarioEnMemoria();
        inventario.registrar(laptop, 5);
        inventario.registrar(mouse, 10);
        procesador = new ProcesadorPedidos(inventario, List.of(
                (c, recibo) -> notificacionesEnviadas.add(c.nombre() + ":" + recibo.total())));
    }

    @Test
    @DisplayName("DESC10 + ESTANDAR + TARJETA: mismo total que la versión original")
    void pedidoConDescuentoYTarjeta() {
        Pedido pedido = Pedido.para(cliente)
                .agregar(laptop, 1)
                .agregar(mouse, 2)
                .conCupon(cupones.buscar("DESC10"))
                .enviarPor(new EnvioEstandar())
                .pagarCon(new PagoTarjeta())
                .construir();

        Recibo recibo = procesador.procesar(pedido);

        assertEquals(190_000, recibo.subtotal(), 0.001);
        assertEquals(19_000, recibo.descuento(), 0.001);
        assertEquals(10_000, recibo.costoEnvio(), 0.001);
        assertEquals(5_430, recibo.recargo(), 0.001);
        assertEquals(186_430, recibo.total(), 0.001);
        assertEquals(4, inventario.consultarStock("P1"));
        assertEquals(8, inventario.consultarStock("P2"));
    }

    @Test
    @DisplayName("Envío estándar gratis desde 200.000")
    void envioEstandarGratis() {
        Recibo recibo = procesador.procesar(pedidoSimple(laptop, 2, null, new EnvioEstandar()));

        assertEquals(0, recibo.costoEnvio(), 0.001);
        assertEquals(300_000, recibo.total(), 0.001);
    }

    @Test
    @DisplayName("DESC20 no aplica bajo el mínimo")
    void desc20BajoMinimo() {
        Recibo recibo = procesador.procesar(pedidoSimple(mouse, 1, "DESC20", new EnvioExpress()));

        assertEquals(45_000, recibo.total(), 0.001);
    }

    @Test
    @DisplayName("DESC20 + INTERNACIONAL + TARJETA")
    void desc20Internacional() {
        Pedido pedido = Pedido.para(cliente)
                .agregar(laptop, 1)
                .conCupon(cupones.buscar("DESC20"))
                .enviarPor(new EnvioInternacional())
                .pagarCon(new PagoTarjeta())
                .construir();

        assertEquals(175_100, procesador.procesar(pedido).total(), 0.001);
    }

    @Test
    @DisplayName("ENVIOGRATIS anula el costo de envío")
    void cuponEnvioGratis() {
        Recibo recibo = procesador.procesar(pedidoSimple(mouse, 1, "ENVIOGRATIS", new EnvioExpress()));

        assertEquals(20_000, recibo.total(), 0.001);
    }

    @Test
    @DisplayName("Validaciones con excepciones de dominio")
    void validaciones() {
        assertThrows(PedidoInvalidoException.class, () -> Pedido.para(cliente)
                .enviarPor(new EnvioEstandar()).pagarCon(new PagoEfectivo()).construir());
        assertThrows(PedidoInvalidoException.class, () -> inventario.buscarProducto("X"));
        assertThrows(PedidoInvalidoException.class, () -> Pedido.para(cliente).agregar(laptop, 0));
        assertThrows(PedidoInvalidoException.class, () -> cupones.buscar("FALSO"));
        assertThrows(PedidoInvalidoException.class, () -> Pedido.para(cliente)
                .agregar(laptop, 1).pagarCon(new PagoEfectivo()).construir());
        assertThrows(StockInsuficienteException.class,
                () -> procesador.procesar(pedidoSimple(laptop, 6, null, new EnvioEstandar())));
    }

    @Test
    @DisplayName("Notifica a través de la abstracción Notificador")
    void notificaciones() {
        procesador.procesar(pedidoSimple(mouse, 1, null, new EnvioExpress()));

        assertEquals(List.of("Ana:45000.0"), notificacionesEnviadas);
    }

    @Test
    @DisplayName("BUG corregido: producto repetido no deja stock negativo")
    void productoRepetidoSeValidaEnConjunto() {
        Pedido pedido = Pedido.para(cliente)
                .agregar(laptop, 3)
                .agregar(laptop, 3)
                .enviarPor(new EnvioEstandar())
                .pagarCon(new PagoEfectivo())
                .construir();

        assertThrows(StockInsuficienteException.class, () -> procesador.procesar(pedido));
        assertEquals(5, inventario.consultarStock("P1"));
        assertTrue(notificacionesEnviadas.isEmpty());
    }

    @Test
    @DisplayName("Extensible (OCP): un cupón nuevo sin tocar el procesador")
    void cuponPersonalizado() {
        Cupon cuponFijo = subtotal -> 5_000;
        Pedido pedido = Pedido.para(cliente)
                .agregar(mouse, 1)
                .conCupon(cuponFijo)
                .enviarPor(new EnvioExpress())
                .pagarCon(new PagoEfectivo())
                .construir();

        assertEquals(40_000, procesador.procesar(pedido).total(), 0.001);
    }

    private Pedido pedidoSimple(Producto producto, int cantidad, String codigoCupon,
                                MetodoEnvio envio) {
        return Pedido.para(cliente)
                .agregar(producto, cantidad)
                .conCupon(cupones.buscar(codigoCupon))
                .enviarPor(envio)
                .pagarCon(new PagoEfectivo())
                .construir();
    }
}
