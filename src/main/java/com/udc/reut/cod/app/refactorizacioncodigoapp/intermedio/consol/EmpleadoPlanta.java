package com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol;

public class EmpleadoPlanta extends Empleado {

    private static final double TOPE_AUXILIO_TRANSPORTE = 2_000_000;
    private static final double AUXILIO_TRANSPORTE = 162_000;

    private final double salarioBase;

    public EmpleadoPlanta(String nombre, double salarioBase) {
        super(nombre);
        if (salarioBase < 0) {
            throw new IllegalArgumentException("El salario no puede ser negativo");
        }
        this.salarioBase = salarioBase;
    }

    @Override
    public double calcularPago() {
        return salarioBase + calcularAuxilioTransporte();
    }

    private double calcularAuxilioTransporte() {
        return salarioBase < TOPE_AUXILIO_TRANSPORTE ? AUXILIO_TRANSPORTE : 0;
    }
}
