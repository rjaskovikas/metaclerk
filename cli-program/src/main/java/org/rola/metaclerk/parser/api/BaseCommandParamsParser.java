package org.rola.metaclerk.parser.api;

public interface BaseCommandParamsParser {
    void parseAndExecute(String[] params);

    void closeProgram();
}
