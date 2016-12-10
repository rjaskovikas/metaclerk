package org.rola.metaclerk.parser.impl;

import org.rola.metaclerk.exception.BaseException;
import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.exception.parser.ParserException;
import org.rola.metaclerk.parser.api.BaseCommandParamsParser;
import org.rola.metaclerk.test.ExitManager;
import org.rola.metaclerk.test.SystemStreams;
import org.rola.metaclerk.utils.ArrayIterator;

abstract class BaseCommandParamsParserImpl extends BaseParamParserImpl implements BaseCommandParamsParser {
    private String[] params;
    boolean printHelp;
    final SystemStreams systemStreams;
    ExitManager exitManager;

    BaseCommandParamsParserImpl(SystemStreams streams, ExitManager exitManager) {
        this.systemStreams = streams;
        this.exitManager = exitManager;
    }

    @Override
    public void parseAndExecute(String[] params) {
        try {
            parseAndExecuteSafe(params);
        } catch (BaseException ex) {
            printErrorAndUsage(ex);
        }
    }

    private void parseAndExecuteSafe(String[] params) {
        this.params = params;
        parseParams();
        if (printHelp)
            printCommandUsage();
        else
            executeCommand();
    }

    protected abstract void executeCommand();

    private void parseParams() {
        try {
            parseParamsSafe();
        } catch (BadParamException ex) {
            throw new ParserException("Error: ", ex);
        }
    }

    private void parseParamsSafe() {
        paramIt = new ArrayIterator<>(params);
        setupEmbeddedParsers();
        while (paramIt.hasNext() && !printHelp) {
            paramIt.next();
            if (!isEmbeddedParserParam())
                parseNextParam();
        }
        checkMandatoryParams();
    }

    protected abstract boolean isEmbeddedParserParam();

    protected abstract void setupEmbeddedParsers();

    protected abstract void checkMandatoryParams();

    void parseNextParam() {
        switch (paramIt.current()) {
            case "--help":
                printHelp = true;
                return;
            default:
                throw new BadParamException("Unknown parameter: " + paramIt.current());
        }
    }

    private void printErrorAndUsage(BaseException ex) {
        if (ex != null)
            printErrorMessage(ex);
        printCommandUsage();
    }

    private void printErrorMessage(Throwable ex) {
        while (ex != null) {
            printError(ex.getMessage());
            ex = ex.getCause();
        }
    }

    void printCommandUsage() {
        printError("");
        printError("Command usage:");
        printError("");
        printError(getParamsDescriptionString());
        printError("");
        printError(getParamsUsageString());
    }

    void printError(String message) {
        systemStreams.getSystemErr().println(message);
    }
}
