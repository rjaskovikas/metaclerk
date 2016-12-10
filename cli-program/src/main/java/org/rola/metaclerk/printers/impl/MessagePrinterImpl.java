package org.rola.metaclerk.printers.impl;

import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;

import java.io.PrintStream;

public class MessagePrinterImpl implements MessagePrinter {
    private final PrintStream printStream;
    private PrinterVerboseLevel verboseLevel;
    private static final MessagePrinter defaultPrinter = new MessagePrinterImpl(System.out, PrinterVerboseLevel.NONE);

    static public MessagePrinter getDefaultPrinter() {
        return defaultPrinter;
    }

    @Override
    public PrinterVerboseLevel getVerboseLevel() {
        return verboseLevel;
    }

    @Override
    public void setVerboseLevel(PrinterVerboseLevel verboseLevel) {
        this.verboseLevel = verboseLevel;
    }

    public MessagePrinterImpl(PrintStream printStream, PrinterVerboseLevel verboseLevel) {
        this.printStream = printStream;
        this.verboseLevel = verboseLevel;
    }

    @Override
    public void println(String message) {
        printStream.println(message);
    }

    @Override
    public void printlnV(String message) {
        if (verboseLevel != PrinterVerboseLevel.NONE)
            printStream.println(message);
    }

    @Override
    public void printlnVV(String message) {
        if (verboseLevel == PrinterVerboseLevel.VERY_VERBOSE)
            printStream.println(message);
    }

    @Override
    public PrintStream getOutputStream() {
        return printStream;
    }

}