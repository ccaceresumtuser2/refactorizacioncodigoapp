package com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NominaTest {

    @Nested
    @DisplayName("Empleado de planta")
    class Planta {

        @Test
        @DisplayName("Recibe auxilio de transporte si gana menos del tope")
        void conAuxilio() {
            assertEquals(1_662_000, new EmpleadoPlanta("Ana", 1_500_000).calcularPago(), 0.001);
        }

        @Test
        @DisplayName("No recibe auxilio si gana el tope o más")
        void sinAuxilio() {
            assertEquals(3_000_000, new EmpleadoPlanta("Luis", 3_000_000).calcularPago(), 0.001);
        }
    }

    @Nested
    @DisplayName("Empleado por horas")
    class PorHoras {

        @Test
        @DisplayName("Sin horas extra")
        void sinHorasExtra() {
            assertEquals(1_500_000, new EmpleadoPorHoras("Eva", 100).calcularPago(), 0.001);
        }

        @Test
        @DisplayName("Con 10 horas extra recargadas al 25 %")
        void conHorasExtra() {
            assertEquals(2_587_500, new EmpleadoPorHoras("Eva", 170).calcularPago(), 0.001);
        }

        @Test
        @DisplayName("Rechaza horas negativas")
        void horasNegativas() {
            assertThrows(IllegalArgumentException.class, () -> new EmpleadoPorHoras("Eva", -1));
        }
    }

    @Test
    @DisplayName("Vendedor recibe 3 % de comisión")
    void vendedor() {
        assertEquals(1_300_000, new Vendedor("Sol", 1_000_000, 10_000_000).calcularPago(), 0.001);
    }

    @Test
    @DisplayName("La nómina suma polimórficamente el pago de todos")
    void totalNomina() {
        Nomina nomina = new Nomina();
        nomina.agregarEmpleado(new EmpleadoPlanta("Ana", 1_500_000));
        nomina.agregarEmpleado(new EmpleadoPorHoras("Eva", 170));
        nomina.agregarEmpleado(new Vendedor("Sol", 1_000_000, 10_000_000));

        assertEquals(5_549_500, nomina.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Extensible (OCP): un tipo nuevo no requiere modificar Nomina")
    void nuevoTipoSinModificarNomina() {
        Nomina nomina = new Nomina();
        nomina.agregarEmpleado(new Empleado("Practicante") {
            @Override
            public double calcularPago() {
                return 800_000;
            }
        });

        assertEquals(800_000, nomina.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("La lista de empleados no se puede modificar desde fuera")
    void encapsulamiento() {
        Nomina nomina = new Nomina();

        assertThrows(UnsupportedOperationException.class,
                () -> nomina.getEmpleados().add(new Vendedor("X", 0, 0)));
    }
}
