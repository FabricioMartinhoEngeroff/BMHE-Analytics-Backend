package com.dvFabricio.BMEH.infra.exception.database;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
