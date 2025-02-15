package com.dvFabricio.BMEH.infra.exception.authorization;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}