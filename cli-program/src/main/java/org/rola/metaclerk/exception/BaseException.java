package org.rola.metaclerk.exception;

public class BaseException extends RuntimeException {
    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
