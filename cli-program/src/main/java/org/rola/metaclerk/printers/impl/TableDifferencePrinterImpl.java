package org.rola.metaclerk.printers.impl;

import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.model.diff.ColumnDifference;
import org.rola.metaclerk.model.diff.TableDiffType;
import org.rola.metaclerk.model.diff.TableDifference;
import org.rola.metaclerk.printers.api.ColumnDifferencePrinter;
import org.rola.metaclerk.printers.api.TableDifferencePrinter;


public class TableDifferencePrinterImpl implements TableDifferencePrinter {
    private static final String SEPARATOR_LINE = "-----------";
    private MessagePrinter messagePrinter;
    private TableDifference tableDiff;
    ColumnDifferencePrinter colDiffPrinter = new ColumnDifferencePrinterImpl();

    public TableDifferencePrinterImpl() {
        this.colDiffPrinter = new ColumnDifferencePrinterImpl();
    }

    void setColDiffPrinter(ColumnDifferencePrinter colDiffPrinter) {
        this.colDiffPrinter = colDiffPrinter;
    }

    @Override
    public void print(TableDifference tableDiff, MessagePrinter messagePrinter) {
        init(tableDiff, messagePrinter);
        printDifference();
    }

    private void init(TableDifference tableDiff, MessagePrinter messagePrinter) {
        this.tableDiff = tableDiff;
        this.messagePrinter = messagePrinter;
    }

    private void printDifference() {
        if (tableDiff.hasDifference(TableDiffType.EXPECTED_NOT_FOUND)) printExpectedNotFound();
        else if (tableDiff.hasDifference(TableDiffType.ACTUAL_NEW)) printActualNew();
        else printColumnDifferences();
    }

    private void printActualNew() {
        messagePrinter.printlnV(tableDiff.getActualTable().getObjectType() +
                " '" + tableDiff.getActualTable().getName() + "' doesn't exist in snapshot schema.");
        messagePrinter.printlnV(SEPARATOR_LINE);
    }

    private void printExpectedNotFound() {
        messagePrinter.printlnV(tableDiff.getExpectedTable().getObjectType() +
                " '" + tableDiff.getExpectedTable().getName() + "' doesn't exist in database schema.");
        messagePrinter.printlnV(SEPARATOR_LINE);
    }

    private void printColumnDifferences() {
        messagePrinter.printlnV(tableDiff.getActualTable().getObjectType() +
                "s '" + tableDiff.getExpectedTable().getName()+"' are different.");
        tableDiff.getColumnDifferenceList().forEach(this::printColumnDifference);
        messagePrinter.printlnV(SEPARATOR_LINE);
    }

    private void printColumnDifference(ColumnDifference colDiff) {
        colDiffPrinter.print(colDiff, messagePrinter);
    }
}
