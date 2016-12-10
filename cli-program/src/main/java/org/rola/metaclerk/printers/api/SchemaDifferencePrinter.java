package org.rola.metaclerk.printers.api;


import org.rola.metaclerk.model.diff.SchemaDifference;

public interface SchemaDifferencePrinter {
    void print(SchemaDifference schemaDiff, MessagePrinter messagePrinter);
}
