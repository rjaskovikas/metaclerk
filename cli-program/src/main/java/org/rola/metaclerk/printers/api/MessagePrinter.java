package org.rola.metaclerk.printers.api;

import java.io.PrintStream;

public interface MessagePrinter {

    PrinterVerboseLevel getVerboseLevel();

    void setVerboseLevel(PrinterVerboseLevel verboseLevel);

    void println(String message);

    void printlnV(String message);

    void printlnVV(String message);

    PrintStream getOutputStream();
}
