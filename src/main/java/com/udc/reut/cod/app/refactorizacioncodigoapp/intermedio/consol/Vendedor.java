package com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol;

public class Vendedor extends Empleado {

    private static final double PORCENTAJE_COMISION = 0.03;

    private final double salarioBase;
    private final double ventas;

    public Vendedor(String nombre, double salarioBase, double ventas) {
        super(nombre);
        if (salarioBase < 0 || ventas < 0) {
            throw new IllegalArgumentException("Salario y ventas no pueden ser negativos");
        }
        this.salarioBase = salarioBase;
        this.ventas = ventas;
    }

    @Override
    public double calcularPago() {
        return salarioBase + ventas * PORCENTAJE_COMISION;
    }
}
