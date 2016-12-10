package org.rola.metaclerk.printers.impl;

import org.rola.metaclerk.model.diff.ColumnDifference;
import org.rola.metaclerk.printers.api.ColumnWarningPrinter;
import org.rola.metaclerk.printers.api.MessagePrinter;

public class ColumnWarningPrinterImpl implements ColumnWarningPrinter {

    @Override
    public void print(ColumnDifference colDiff, MessagePrinter messagePrinter) {
        colDiff.getWarnings().forEach(warning ->
                messagePrinter.printlnVV("Warning:column(" + colDiff.getColumnName()+"):"+warning)
        );
    }
}
