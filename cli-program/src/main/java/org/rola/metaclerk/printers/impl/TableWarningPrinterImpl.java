package org.rola.metaclerk.printers.impl;

import org.rola.metaclerk.model.diff.TableDifference;
import org.rola.metaclerk.printers.api.ColumnWarningPrinter;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.TableWarningPrinter;

public class TableWarningPrinterImpl implements TableWarningPrinter {
    private static final String SEPARATOR_LINE = "-----------";
    private ColumnWarningPrinter columnWarningPrinter = new ColumnWarningPrinterImpl();
    private MessagePrinter messagePrinter;
    private TableDifference tableDiff;
    private boolean headerPrinter;

    public void setColumnWarningPrinter(ColumnWarningPrinter columnWarningPrinter) {
        this.columnWarningPrinter = columnWarningPrinter;
    }

    @Override
    public void print(TableDifference tableDiff, MessagePrinter messagePrinter) {
        init(tableDiff, messagePrinter);
        printHeader();
        printWarnings();
        printSeparator();
    }

    private void printHeader() {
        if (tableDiff.hasWarnings()) {
            messagePrinter.printlnVV("Table/view (" + tableDiff.getTableName() + ") has warnings:");
        }
    }

    private void printWarnings() {
        tableDiff.getColumnDifferenceList().forEach(colDiff-> {
            columnWarningPrinter.print(colDiff, messagePrinter);
        });
    }

    private void printSeparator() {
        if (tableDiff.hasWarnings()) {
            messagePrinter.printlnVV(SEPARATOR_LINE);
        }
    }

    private void init(TableDifference tableDiff, MessagePrinter messagePrinter) {
        this.tableDiff = tableDiff;
        this.messagePrinter = messagePrinter;
        this.headerPrinter = false;
    }
}
