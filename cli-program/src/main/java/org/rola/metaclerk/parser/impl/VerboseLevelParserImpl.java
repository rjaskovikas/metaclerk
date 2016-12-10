package org.rola.metaclerk.parser.impl;

import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.parser.api.VerboseLevelParser;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.utils.ArrayIterator;

public class VerboseLevelParserImpl extends BaseParamParserImpl implements VerboseLevelParser {

    private PrinterVerboseLevel verboseLevel = PrinterVerboseLevel.NONE;

    @Override
    public void setup(ArrayIterator<String> paramIt) {
        this.paramIt = paramIt;
    }


    @Override
    public PrinterVerboseLevel getVerboseLevel() {
        return verboseLevel;
    }

    @Override
    public boolean parseParam() {
        switch (paramIt.current()) {
            case VERBOSE_PARAM:
                parseVerboseLevel();
                return true;
            case VERY_VERBOSE_PARAM:
                parseVeryVerboseLevel();
                return true;
        }
        return false;
    }

    @Override
    public void checkMandatoryParams() {
    }

    private void parseVeryVerboseLevel() {
        if (verboseLevel != PrinterVerboseLevel.NONE)
            throw new BadParamException("Verbosity level specified twice");
        this.verboseLevel = PrinterVerboseLevel.VERY_VERBOSE;
    }

    private void parseVerboseLevel() {
        if (verboseLevel != PrinterVerboseLevel.NONE)
            throw new BadParamException("Verbosity level specified twice");
        this.verboseLevel = PrinterVerboseLevel.VERBOSE;
    }

    @Override
    public String getParamsDescriptionString() {
        return "{-v | -vv}";
    }

    @Override
    public String getParamsUsageString() {
        return  "\t-v\t - verbose output\n" +
                "\t-vv\t - very verbose output\n";
    }
}
