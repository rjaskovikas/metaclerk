package org.rola.metaclerk.printers.impl;


import org.rola.metaclerk.model.diff.SchemaDifference;
import org.rola.metaclerk.model.diff.TableDifference;
import org.rola.metaclerk.printers.api.*;

public class SchemaDifferencePrinterImpl implements SchemaDifferencePrinter {
    TableDifferencePrinter tableDiffPrinter;
    TableWarningPrinter tableWarningPrinter;
    PrivilegeDifferencePrinter privilegePrinter;
    private SchemaDifference schemaDiff;
    private MessagePrinter messagePrinter;

    public SchemaDifferencePrinterImpl() {
        this.tableDiffPrinter = new TableDifferencePrinterImpl();
        this.privilegePrinter = new PrivilegeDifferencePrinterImpl();
        this.tableWarningPrinter = new TableWarningPrinterImpl();

    }

    public void setTableDiffPrinter(TableDifferencePrinter tablePrinter) {
        this.tableDiffPrinter = tablePrinter;
    }

    public void setTableWarningPrinter(TableWarningPrinter tableWarningPrinter) {
        this.tableWarningPrinter = tableWarningPrinter;
    }

    public void setPrivilegePrinter(PrivilegeDifferencePrinter privilegePrinter) {
        this.privilegePrinter = privilegePrinter;
    }

    @Override
    public void print(SchemaDifference schemaDiff, MessagePrinter messagePrinter) {
//        if (messagePrinter == null) return;
        init(schemaDiff, messagePrinter);
        printDifference();
        printWarnings();
    }

    private void printWarnings() {
        printTableWarnings();
        printPrivilegeWarnings();
    }

    private void printPrivilegeWarnings() {
        // intentionally empty
    }

    private void printTableWarnings() {
        schemaDiff.getTableDifferenceList().forEach(this::printTableWarnings);
    }

    private void printTableWarnings(TableDifference tableDifference) {
        tableWarningPrinter.print(tableDifference, messagePrinter);
    }

    private void init(SchemaDifference schemaDiff, MessagePrinter messagePrinter) {
        this.schemaDiff = schemaDiff;
        this.messagePrinter = messagePrinter;
    }

    private void printDifference() {
        schemaDiff.getTableDifferenceList().forEach(this::printTableDifferences);
        printPrivilegeDifference();
    }

    private void printPrivilegeDifference() {
        privilegePrinter.print(schemaDiff.getPrivilegeDifferences(), messagePrinter);
    }

    private void printTableDifferences(TableDifference tblDiff) {
        tableDiffPrinter.print(tblDiff, messagePrinter);
    }
}
