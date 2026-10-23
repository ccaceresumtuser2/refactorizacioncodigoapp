package com.udc.reut.cod.app.refactorizacioncodigoapp.basico.consol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FacturaTest {

    private Factura crearFactura(TipoCliente tipo) {
        Factura factura = new Factura(tipo);
        factura.agregarLinea(10_000, 2);
        factura.agregarLinea(5_000, 3);
        return factura;
    }

    @ParameterizedTest(name = "Cliente {0} -> total {1}")
    @CsvSource({
        "REGULAR,   41650.0",
        "FRECUENTE, 39567.5",
        "VIP,       37485.0"
    })
    @DisplayName("Mismo resultado que la versión sin refactorizar")
    void calculaTotalSegunTipoCliente(TipoCliente tipo, double esperado) {
        assertEquals(esperado, crearFactura(tipo).calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Cada paso del cálculo se puede verificar por separado")
    void desgloseDeLaFactura() {
        Factura factura = crearFactura(TipoCliente.VIP);

        assertEquals(35_000, factura.calcularSubtotal(), 0.001);
        assertEquals(3_500, factura.calcularDescuento(), 0.001);
        assertEquals(5_985, factura.calcularIva(), 0.001);
    }

    @Test
    @DisplayName("Factura sin líneas tiene total cero")
    void facturaVacia() {
        assertEquals(0, new Factura(TipoCliente.VIP).calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("LineaFactura rechaza cantidades y precios inválidos")
    void validaLineas() {
        Factura factura = new Factura(TipoCliente.REGULAR);

        assertThrows(IllegalArgumentException.class, () -> factura.agregarLinea(1_000, 0));
        assertThrows(IllegalArgumentException.class, () -> factura.agregarLinea(-1, 1));
    }
}
