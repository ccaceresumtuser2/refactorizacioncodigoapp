package com.udc.reut.cod.app.refactorizacioncodigoapp.basico.sinsol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Pruebas de caracterización: fijan el comportamiento actual ANTES de
 * refactorizar, para garantizar que la versión refactorizada hace lo mismo.
 */
class FacturaTest {

    private final double[] precios = {10_000, 5_000};
    private final int[] cantidades = {2, 3};

    @ParameterizedTest(name = "Cliente {0} -> total {1}")
    @CsvSource({
        "REGULAR,   41650.0",
        "FRECUENTE, 39567.5",
        "VIP,       37485.0"
    })
    @DisplayName("Calcula el total con descuento por tipo de cliente e IVA")
    void calculaTotalSegunTipoCliente(String tipo, double esperado) {
        Factura factura = new Factura();

        assertEquals(esperado, factura.calc(precios, cantidades, tipo), 0.001);
    }

    @ParameterizedTest
    @CsvSource({"''", "DESCONOCIDO"})
    @DisplayName("Un tipo no reconocido se trata como cliente sin descuento")
    void tipoDesconocidoNoTieneDescuento(String tipo) {
        assertEquals(41650.0, new Factura().calc(precios, cantidades, tipo), 0.001);
    }
}
