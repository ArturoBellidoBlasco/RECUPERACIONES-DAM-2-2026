package com.arturo.tienda.exceptions;

public class ProductoYaExistenteException extends RuntimeException {
    public ProductoYaExistenteException(String message) {
        super(message);
    }
}
