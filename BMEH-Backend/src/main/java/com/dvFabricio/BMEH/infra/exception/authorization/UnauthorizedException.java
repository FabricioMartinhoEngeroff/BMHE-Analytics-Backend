package com.dvFabricio.BMEH.infra.exception.authorization;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

