package org.rola.metaclerk.parser.api;

import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.utils.ArrayIterator;

public interface VerboseLevelParser extends BaseParamParser{
    String VERBOSE_PARAM = "-v";
    String VERY_VERBOSE_PARAM = "-vv";

    void setup(ArrayIterator<String> paramIt);

    PrinterVerboseLevel getVerboseLevel();

    boolean parseParam();

    void checkMandatoryParams();
}
