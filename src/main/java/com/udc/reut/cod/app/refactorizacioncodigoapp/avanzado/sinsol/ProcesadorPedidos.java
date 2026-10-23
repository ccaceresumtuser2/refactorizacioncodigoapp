package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.sinsol;

import java.util.HashMap;
import java.util.Map;

/**
 * EJEMPLO 3 - NIVEL AVANZADO (SIN REFACTORIZAR)
 *
 * Procesa un pedido de una tienda en línea:
 *  1. Valida productos, cantidades y stock.
 *  2. Calcula subtotal.
 *  3. Aplica cupón: DESC10 (10 %), DESC20 (20 % si subtotal >= 100.000),
 *     ENVIOGRATIS (envío en 0). Un cupón desconocido es un error.
 *  4. Calcula envío: ESTANDAR 10.000 (gratis si el monto con descuento
 *     es >= 200.000), EXPRESS 25.000, INTERNACIONAL 50.000.
 *  5. Calcula recargo de pago: TARJETA 3 % sobre (monto + envío), EFECTIVO 0.
 *  6. Descuenta inventario.
 *  7. Notifica al cliente por email y/o SMS.
 *
 * Code smells presentes:
 *  - "Método Dios" / método largo: una sola función con 7 responsabilidades
 *    (viola el Principio de Responsabilidad Única, SRP).
 *  - Estado global mutable (mapas static públicos) -> difícil de probar.
 *  - Cadenas de if/else para envío, pago y cupones (viola OCP).
 *  - Acoplamiento a System.out para notificar (no se puede sustituir).
 *  - Obsesión por primitivos y arreglos paralelos.
 *  - Números mágicos por todas partes.
 */
public class ProcesadorPedidos {

    public static Map<String, Double> precios = new HashMap<>();
    public static Map<String, Integer> stock = new HashMap<>();

    public double procesar(String email, String telefono, String[] codigos, int[] cantidades,
                           String cupon, String envio, String pago) {
        if (codigos == null || codigos.length == 0) {
            throw new IllegalArgumentException("El pedido no tiene productos");
        }
        double subtotal = 0;
        for (int i = 0; i < codigos.length; i++) {
            if (!precios.containsKey(codigos[i])) {
                throw new IllegalArgumentException("Producto no existe: " + codigos[i]);
            }
            if (cantidades[i] <= 0) {
                throw new IllegalArgumentException("Cantidad inválida para " + codigos[i]);
            }
            if (stock.get(codigos[i]) < cantidades[i]) {
                throw new IllegalStateException("Stock insuficiente para " + codigos[i]);
            }
            subtotal = subtotal + precios.get(codigos[i]) * cantidades[i];
        }

        double descuento = 0;
        boolean envioGratis = false;
        if (cupon != null && !cupon.isEmpty()) {
            if (cupon.equals("DESC10")) {
                descuento = subtotal * 0.10;
            } else if (cupon.equals("DESC20")) {
                if (subtotal >= 100000) {
                    descuento = subtotal * 0.20;
                }
            } else if (cupon.equals("ENVIOGRATIS")) {
                envioGratis = true;
            } else {
                throw new IllegalArgumentException("Cupón inválido: " + cupon);
            }
        }
        double monto = subtotal - descuento;

        double costoEnvio = 0;
        if (envio.equals("ESTANDAR")) {
            if (monto >= 200000) {
                costoEnvio = 0;
            } else {
                costoEnvio = 10000;
            }
        } else if (envio.equals("EXPRESS")) {
            costoEnvio = 25000;
        } else if (envio.equals("INTERNACIONAL")) {
            costoEnvio = 50000;
        } else {
            throw new IllegalArgumentException("Método de envío inválido: " + envio);
        }
        if (envioGratis) {
            costoEnvio = 0;
        }

        double recargo = 0;
        if (pago.equals("TARJETA")) {
            recargo = (monto + costoEnvio) * 0.03;
        } else if (pago.equals("EFECTIVO")) {
            recargo = 0;
        } else {
            throw new IllegalArgumentException("Método de pago inválido: " + pago);
        }

        double total = monto + costoEnvio + recargo;
        total = Math.round(total * 100.0) / 100.0;

        for (int i = 0; i < codigos.length; i++) {
            stock.put(codigos[i], stock.get(codigos[i]) - cantidades[i]);
        }

        if (email != null) {
            System.out.println("EMAIL a " + email + ": Su pedido por $" + total + " fue confirmado");
        }
        if (telefono != null) {
            System.out.println("SMS a " + telefono + ": Su pedido por $" + total + " fue confirmado");
        }
        return total;
    }
}
