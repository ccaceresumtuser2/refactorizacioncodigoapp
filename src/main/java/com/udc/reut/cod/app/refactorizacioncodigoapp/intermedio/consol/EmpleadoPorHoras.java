package com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol;

public class EmpleadoPorHoras extends Empleado {

    private static final double TARIFA_HORA = 15_000;
    private static final int HORAS_ORDINARIAS = 160;
    private static final double RECARGO_HORA_EXTRA = 0.25;

    private final int horasTrabajadas;

    public EmpleadoPorHoras(String nombre, int horasTrabajadas) {
        super(nombre);
        if (horasTrabajadas < 0) {
            throw new IllegalArgumentException("Las horas no pueden ser negativas");
        }
        this.horasTrabajadas = horasTrabajadas;
    }

    @Override
    public double calcularPago() {
        return horasTrabajadas * TARIFA_HORA + calcularRecargoHorasExtra();
    }

    private double calcularRecargoHorasExtra() {
        int horasExtra = Math.max(0, horasTrabajadas - HORAS_ORDINARIAS);
        return horasExtra * TARIFA_HORA * RECARGO_HORA_EXTRA;
    }
}
