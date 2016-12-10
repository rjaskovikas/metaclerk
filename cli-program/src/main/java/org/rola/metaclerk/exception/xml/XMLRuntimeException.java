package org.rola.metaclerk.exception.xml;

import org.rola.metaclerk.exception.BaseException;

public class XMLRuntimeException extends BaseException {

    public XMLRuntimeException(String message) {
        super(message);
    }

    public XMLRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
