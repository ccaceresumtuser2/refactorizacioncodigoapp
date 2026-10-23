package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.inventario;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.PedidoInvalidoException;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.StockInsuficienteException;
import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Producto;
import java.util.HashMap;
import java.util.Map;

public class InventarioEnMemoria implements Inventario {

    private final Map<String, Producto> productos = new HashMap<>();
    private final Map<String, Integer> existencias = new HashMap<>();

    public void registrar(Producto producto, int cantidadInicial) {
        productos.put(producto.codigo(), producto);
        existencias.put(producto.codigo(), cantidadInicial);
    }

    @Override
    public Producto buscarProducto(String codigo) {
        Producto producto = productos.get(codigo);
        if (producto == null) {
            throw new PedidoInvalidoException("Producto no existe: " + codigo);
        }
        return producto;
    }

    @Override
    public int consultarStock(String codigo) {
        return existencias.getOrDefault(codigo, 0);
    }

    @Override
    public void descontar(String codigo, int cantidad) {
        int disponible = consultarStock(codigo);
        if (disponible < cantidad) {
            throw new StockInsuficienteException(codigo, cantidad, disponible);
        }
        existencias.put(codigo, disponible - cantidad);
    }
}
