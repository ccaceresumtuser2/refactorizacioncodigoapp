package com.udc.reut.cod.app.refactorizacioncodigoapp.avanzado.consol.modelo;

/**
 * Agrupa los datos de contacto que antes viajaban como parámetros sueltos.
 */
public record Cliente(String nombre, String email, String telefono) {

    public boolean tieneEmail() {
        return email != null && !email.isBlank();
    }

    public boolean tieneTelefono() {
        return telefono != null && !telefono.isBlank();
    }
}
