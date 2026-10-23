/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.udc.reut.cod.app.refactorizacioncodigoapp;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon.CatalogoCupones;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.envio.EnvioEstandar;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.inventario.InventarioEnMemoria;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Cliente;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Pedido;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Producto;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.notificacion.NotificadorEmail;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.notificacion.NotificadorSms;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.pago.PagoTarjeta;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.servicio.ProcesadorPedidos;
import com.udc.reut.cod.app.refactorizacioncodigoapp.basico.consol.Factura;
import com.udc.reut.cod.app.refactorizacioncodigoapp.basico.consol.TipoCliente;
import com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol.EmpleadoPlanta;
import com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol.EmpleadoPorHoras;
import com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol.Nomina;
import com.udc.reut.cod.app.refactorizacioncodigoapp.intermedio.consol.Vendedor;
import java.util.List;

/**
 *
 * @author USER
 */
public class Refactorizacioncodigoapp {

    public static void main(String[] args) {
        System.out.println("=== Ejemplo 1 (básico): Factura ===");
        Factura factura = new Factura(TipoCliente.VIP);
        factura.agregarLinea(10_000, 2);
        factura.agregarLinea(5_000, 3);
        System.out.println("Total: " + factura.calcularTotal());

        System.out.println("\n=== Ejemplo 2 (intermedio): Nómina ===");
        Nomina nomina = new Nomina();
        nomina.agregarEmpleado(new EmpleadoPlanta("Ana", 1_500_000));
        nomina.agregarEmpleado(new EmpleadoPorHoras("Eva", 170));
        nomina.agregarEmpleado(new Vendedor("Sol", 1_000_000, 10_000_000));
        System.out.println("Total nómina: " + nomina.calcularTotal());

        System.out.println("\n=== Ejemplo 3 (avanzado): Pedido ===");
        Producto laptop = new Producto("P1", "Laptop", 150_000);
        Producto mouse = new Producto("P2", "Mouse", 20_000);
        InventarioEnMemoria inventario = new InventarioEnMemoria();
        inventario.registrar(laptop, 5);
        inventario.registrar(mouse, 10);
        ProcesadorPedidos procesador = new ProcesadorPedidos(inventario,
                List.of(new NotificadorEmail(), new NotificadorSms()));

        Pedido pedido = Pedido.para(new Cliente("Ana", "ana@mail.com", "3001234567"))
                .agregar(laptop, 1)
                .agregar(mouse, 2)
                .conCupon(CatalogoCupones.porDefecto().buscar("DESC10"))
                .enviarPor(new EnvioEstandar())
                .pagarCon(new PagoTarjeta())
                .construir();
        System.out.println(procesador.procesar(pedido));
    }
}
