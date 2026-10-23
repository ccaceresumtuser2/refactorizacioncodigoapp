package com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol;

/**
 * Refactorización: "Reemplazar condicional por polimorfismo".
 *
 * Clase abstracta que define el contrato común. Cada subclase sabe calcular
 * su propio pago, así que agregar un nuevo tipo de empleado no obliga a
 * modificar el código existente (principio Abierto/Cerrado).
 */
public abstract class Empleado {

    private final String nombre;

    protected Empleado(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract double calcularPago();
}
