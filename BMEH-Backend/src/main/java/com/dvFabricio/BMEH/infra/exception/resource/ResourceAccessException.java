package com.dvFabricio.BMEH.infra.exception.resource;

public class ResourceAccessException extends RuntimeException {

    public ResourceAccessException(String message) {
        super(message);
    }
}
