package com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.sinsol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Pruebas de caracterización de la nómina sin refactorizar.
 * Note cómo hay que pasar parámetros "basura" (0) que no aplican al tipo.
 */
class NominaTest {

    private final Nomina nomina = new Nomina();

    @ParameterizedTest(name = "{0} salario={1} horas={2} ventas={3} -> {4}")
    @CsvSource({
        "PLANTA,   1500000, 0,   0,        1662000",
        "PLANTA,   3000000, 0,   0,        3000000",
        "HORAS,    0,       100, 0,        1500000",
        "HORAS,    0,       170, 0,        2587500",
        "VENDEDOR, 1000000, 0,   10000000, 1300000"
    })
    @DisplayName("Calcula el pago según el tipo de empleado")
    void calculaPago(String tipo, double salario, int horas, double ventas, double esperado) {
        assertEquals(esperado, nomina.calcularPago(tipo, salario, horas, ventas), 0.001);
    }

    @Test
    @DisplayName("Suma el total de la nómina con arreglos paralelos")
    void calculaTotal() {
        double total = nomina.calcularTotalNomina(
                new String[]{"PLANTA", "HORAS", "VENDEDOR"},
                new double[]{1_500_000, 0, 1_000_000},
                new int[]{0, 170, 0},
                new double[]{0, 0, 10_000_000});

        assertEquals(5_549_500, total, 0.001);
    }

    @Test
    @DisplayName("Un tipo desconocido lanza excepción en tiempo de ejecución")
    void tipoDesconocido() {
        assertThrows(IllegalArgumentException.class, () -> nomina.calcularPago("GERENTE", 1, 1, 1));
    }
}
