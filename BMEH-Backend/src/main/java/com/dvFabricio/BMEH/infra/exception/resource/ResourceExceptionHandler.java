package com.dvFabricio.BMEH.infra.exception.resource;

public class ResourceExceptionHandler extends RuntimeException {

    public ResourceExceptionHandler(String message) {
        super(message);
    }
}

