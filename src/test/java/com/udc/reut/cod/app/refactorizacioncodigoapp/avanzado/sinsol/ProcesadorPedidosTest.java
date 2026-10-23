package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.sinsol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de caracterización del procesador sin refactorizar.
 *
 * Observe lo difícil que es probarlo:
 *  - Hay que reiniciar a mano el estado estático global en cada prueba.
 *  - Para verificar las notificaciones hay que secuestrar System.out.
 */
class ProcesadorPedidosTest {

    private final ProcesadorPedidos procesador = new ProcesadorPedidos();
    private final PrintStream salidaOriginal = System.out;
    private final ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();

    @BeforeEach
    void reiniciarEstadoGlobal() {
        ProcesadorPedidos.precios.clear();
        ProcesadorPedidos.stock.clear();
        ProcesadorPedidos.precios.put("P1", 150_000.0);
        ProcesadorPedidos.precios.put("P2", 20_000.0);
        ProcesadorPedidos.stock.put("P1", 5);
        ProcesadorPedidos.stock.put("P2", 10);
        System.setOut(new PrintStream(salidaCapturada));
    }

    @AfterEach
    void restaurarSalida() {
        System.setOut(salidaOriginal);
    }

    @Test
    @DisplayName("DESC10 + ESTANDAR + TARJETA")
    void pedidoConDescuentoYTarjeta() {
        double total = procesador.procesar("ana@mail.com", null,
                new String[]{"P1", "P2"}, new int[]{1, 2}, "DESC10", "ESTANDAR", "TARJETA");

        assertEquals(186_430.0, total, 0.001);
        assertEquals(4, ProcesadorPedidos.stock.get("P1"));
        assertEquals(8, ProcesadorPedidos.stock.get("P2"));
    }

    @Test
    @DisplayName("Envío estándar gratis desde 200.000")
    void envioEstandarGratis() {
        double total = procesador.procesar(null, null,
                new String[]{"P1"}, new int[]{2}, null, "ESTANDAR", "EFECTIVO");

        assertEquals(300_000.0, total, 0.001);
    }

    @Test
    @DisplayName("DESC20 no aplica bajo el mínimo")
    void desc20BajoMinimo() {
        double total = procesador.procesar(null, null,
                new String[]{"P2"}, new int[]{1}, "DESC20", "EXPRESS", "EFECTIVO");

        assertEquals(45_000.0, total, 0.001);
    }

    @Test
    @DisplayName("DESC20 + INTERNACIONAL + TARJETA")
    void desc20Internacional() {
        double total = procesador.procesar(null, null,
                new String[]{"P1"}, new int[]{1}, "DESC20", "INTERNACIONAL", "TARJETA");

        assertEquals(175_100.0, total, 0.001);
    }

    @Test
    @DisplayName("ENVIOGRATIS anula el costo de envío")
    void cuponEnvioGratis() {
        double total = procesador.procesar(null, null,
                new String[]{"P2"}, new int[]{1}, "ENVIOGRATIS", "EXPRESS", "EFECTIVO");

        assertEquals(20_000.0, total, 0.001);
    }

    @Test
    @DisplayName("Errores de validación")
    void validaciones() {
        assertThrows(IllegalArgumentException.class, () -> procesador.procesar(null, null,
                new String[]{}, new int[]{}, null, "ESTANDAR", "EFECTIVO"));
        assertThrows(IllegalArgumentException.class, () -> procesador.procesar(null, null,
                new String[]{"X"}, new int[]{1}, null, "ESTANDAR", "EFECTIVO"));
        assertThrows(IllegalArgumentException.class, () -> procesador.procesar(null, null,
                new String[]{"P1"}, new int[]{0}, null, "ESTANDAR", "EFECTIVO"));
        assertThrows(IllegalStateException.class, () -> procesador.procesar(null, null,
                new String[]{"P1"}, new int[]{6}, null, "ESTANDAR", "EFECTIVO"));
        assertThrows(IllegalArgumentException.class, () -> procesador.procesar(null, null,
                new String[]{"P1"}, new int[]{1}, "FALSO", "ESTANDAR", "EFECTIVO"));
    }

    @Test
    @DisplayName("Notifica por email y SMS escribiendo en consola")
    void notificaciones() {
        procesador.procesar("ana@mail.com", "3001234567",
                new String[]{"P2"}, new int[]{1}, null, "EXPRESS", "EFECTIVO");

        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("EMAIL a ana@mail.com"));
        assertTrue(salida.contains("SMS a 3001234567"));
    }

    @Test
    @DisplayName("BUG documentado: el mismo producto repetido deja stock negativo")
    void bugStockNegativoConProductoRepetido() {
        procesador.procesar(null, null,
                new String[]{"P1", "P1"}, new int[]{3, 3}, null, "ESTANDAR", "EFECTIVO");

        assertEquals(-1, ProcesadorPedidos.stock.get("P1"));
    }
}
