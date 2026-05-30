package com.arturo.tienda.exceptions;

public class ProveedorNotFoundException extends RuntimeException {
    public ProveedorNotFoundException(String message) {
        super(message);
    }
}
