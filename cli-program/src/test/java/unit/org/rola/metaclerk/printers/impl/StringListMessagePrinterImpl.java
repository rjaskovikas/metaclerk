package org.rola.metaclerk.printers.impl;

import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;

import java.io.PrintStream;
import java.util.List;

public class StringListMessagePrinterImpl implements MessagePrinter {

    private final List<String> messages;

    public StringListMessagePrinterImpl(List<String> messages) {
        this.messages = messages;
    }

    public static void printToSystemOut(List<String> messages) {
        messages.forEach(message->System.out.println(message));
    }

    @Override
    public PrinterVerboseLevel getVerboseLevel() {
        return null;
    }

    @Override
    public void setVerboseLevel(PrinterVerboseLevel verboseLevel) {

    }

    @Override
    public void println(String message) {
        messages.add(message);
    }

    @Override
    public void printlnV(String message) {
        messages.add(message);
    }

    @Override
    public void printlnVV(String message) {
        messages.add(message);
    }

    @Override
    public PrintStream getOutputStream() {
        return null;
    }
}
