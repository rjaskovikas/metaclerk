package org.rola.metaclerk.exception.parser;

import org.rola.metaclerk.exception.BaseException;

public class ParserException extends BaseException {
    public ParserException(String message, Exception ex) {
        super(message, ex);
    }
}
