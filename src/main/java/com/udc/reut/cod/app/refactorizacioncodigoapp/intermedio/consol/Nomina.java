package com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EJEMPLO 2 - NIVEL INTERMEDIO (REFACTORIZADO)
 *
 * Refactorizaciones aplicadas:
 *  - Reemplazar condicional por polimorfismo (jerarquía Empleado).
 *  - Reemplazar lista larga de parámetros por objetos.
 *  - Reemplazar arreglos paralelos por una colección de objetos.
 *  - Constantes con nombre en cada subclase (alta cohesión).
 *
 * La Nómina ya no conoce los tipos de empleado: solo les pide su pago.
 */
public class Nomina {

    private final List<Empleado> empleados = new ArrayList<>();

    public void agregarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }
        empleados.add(empleado);
    }

    public List<Empleado> getEmpleados() {
        return Collections.unmodifiableList(empleados);
    }

    public double calcularTotal() {
        return empleados.stream()
                .mapToDouble(Empleado::calcularPago)
                .sum();
    }
}
