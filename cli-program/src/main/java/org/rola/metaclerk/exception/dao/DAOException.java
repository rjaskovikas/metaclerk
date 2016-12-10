package org.rola.metaclerk.exception.dao;

import org.rola.metaclerk.exception.BaseException;

public class DAOException extends BaseException {
    public DAOException(String message, Exception ex) {
        super(message, ex);
    }
}
