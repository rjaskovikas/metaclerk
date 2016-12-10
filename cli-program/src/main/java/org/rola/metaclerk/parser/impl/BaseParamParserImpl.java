package org.rola.metaclerk.parser.impl;

import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.parser.api.BaseParamParser;
import org.rola.metaclerk.utils.ArrayIterator;

abstract class BaseParamParserImpl implements BaseParamParser {
    ArrayIterator<String> paramIt;

    void checkHasNext(String message) throws BadParamException {
        if (!paramIt.hasNext() || paramIt.peekNext().startsWith("-"))
            throw new BadParamException(message);
    }

    void checkMandatoryParam(Object param, String message) {
        if (param == null)
            throw new BadParamException(message);
    }
}
