package com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.sinsol;

/**
 * EJEMPLO 2 - NIVEL INTERMEDIO (SIN REFACTORIZAR)
 *
 * Calcula el pago de empleados según su tipo:
 *  - PLANTA:   salario base + auxilio de transporte si gana menos de 2.000.000.
 *  - HORAS:    horas * 15.000; las horas por encima de 160 tienen recargo del 25 %.
 *  - VENDEDOR: salario base + 3 % de comisión sobre las ventas.
 *
 * Code smells presentes:
 *  - Condicional por tipo (if/else sobre String) que crece con cada tipo nuevo
 *    -> viola el principio Abierto/Cerrado (OCP).
 *  - Lista larga de parámetros; muchos no aplican a todos los tipos
 *    (las "horas" no le importan al empleado de planta).
 *  - Arreglos paralelos en calcularTotalNomina.
 *  - Números mágicos.
 */
public class Nomina {

    public double calcularPago(String tipo, double salarioBase, int horas, double ventas) {
        double pago = 0;
        if (tipo.equals("PLANTA")) {
            pago = salarioBase;
            if (salarioBase < 2000000) {
                pago = pago + 162000;
            }
        } else if (tipo.equals("HORAS")) {
            pago = horas * 15000;
            if (horas > 160) {
                pago = pago + (horas - 160) * 15000 * 0.25;
            }
        } else if (tipo.equals("VENDEDOR")) {
            pago = salarioBase + ventas * 0.03;
        } else {
            throw new IllegalArgumentException("Tipo de empleado desconocido: " + tipo);
        }
        return pago;
    }

    public double calcularTotalNomina(String[] tipos, double[] salarios, int[] horas, double[] ventas) {
        double total = 0;
        for (int i = 0; i < tipos.length; i++) {
            total = total + calcularPago(tipos[i], salarios[i], horas[i], ventas[i]);
        }
        return total;
    }
}
