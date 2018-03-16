package com.kraytsman.exception;

public class PostgresConnectionException extends RuntimeException {
    public PostgresConnectionException(String message) {
        super(message);
    }

    public PostgresConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostgresConnectionException(Throwable cause) {
        super(cause);
    }
}
