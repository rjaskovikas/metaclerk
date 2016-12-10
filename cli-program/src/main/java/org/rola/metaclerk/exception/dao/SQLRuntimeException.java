package org.rola.metaclerk.exception.dao;

import org.rola.metaclerk.exception.BaseException;

public class SQLRuntimeException extends BaseException {
    public SQLRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
