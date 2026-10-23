package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.inventario;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo.Producto;

/**
 * Principio de Inversión de Dependencias (DIP): el procesador depende de esta
 * abstracción y no de un mapa estático. Mañana puede ser una base de datos.
 */
public interface Inventario {

    Producto buscarProducto(String codigo);

    int consultarStock(String codigo);

    void descontar(String codigo, int cantidad);
}
