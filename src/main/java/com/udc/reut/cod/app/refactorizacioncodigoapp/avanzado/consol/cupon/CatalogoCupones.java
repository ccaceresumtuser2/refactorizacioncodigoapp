package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.cupon;

import com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.excepciones.PedidoInvalidoException;
import java.util.HashMap;
import java.util.Map;

/**
 * Patrón Factory / Registro: traduce el código que escribe el usuario
 * ("DESC10") al objeto Cupon correspondiente. Reemplaza la cadena de if/else.
 */
public class CatalogoCupones {

    private final Map<String, Cupon> cupones = new HashMap<>();

    public static CatalogoCupones porDefecto() {
        CatalogoCupones catalogo = new CatalogoCupones();
        catalogo.registrar("DESC10", new CuponPorcentaje(0.10));
        catalogo.registrar("DESC20", new CuponPorcentaje(0.20, 100_000));
        catalogo.registrar("ENVIOGRATIS", new CuponEnvioGratis());
        return catalogo;
    }

    public void registrar(String codigo, Cupon cupon) {
        cupones.put(codigo, cupon);
    }

    public Cupon buscar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return new SinCupon();
        }
        Cupon cupon = cupones.get(codigo);
        if (cupon == null) {
            throw new PedidoInvalidoException("Cupón inválido: " + codigo);
        }
        return cupon;
    }
}
