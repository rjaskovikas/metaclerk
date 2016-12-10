package org.rola.metaclerk.printers.impl;

import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.diff.ColumnDiffType;
import org.rola.metaclerk.model.diff.ColumnDifference;
import org.rola.metaclerk.printers.api.ColumnDifferencePrinter;

public class ColumnDifferencePrinterImpl implements ColumnDifferencePrinter {

    private ColumnDescription expected;
    private ColumnDescription actual;
    private ColumnDifference colDiff;
    private MessagePrinter messagePrinter;

    @Override
    public void print(ColumnDifference colDiff, MessagePrinter messagePrinter) {
        init(colDiff, messagePrinter);
        printDifferences();
    }

    private void printDifferences() {
        if (colDiff.hasDifference(ColumnDiffType.ACTUAL_NEW))
            printNewColumn();
        else if (colDiff.hasDifference(ColumnDiffType.EXPECTED_NOT_FOUND))
            printExpectedColumn();
        else printExistingColumnDifferences();
    }

    private void printExpectedColumn() {
        messagePrinter.printlnVV("'" + expected.getName() +  "' column doesn't exist in database table");
    }

    private void printNewColumn() {
        messagePrinter.printlnVV("'" + actual.getName() + "' column doesn't exist in snapshot table");
    }

    private void printExistingColumnDifferences() {
        printHeader();
        if (colDiff.hasDifferences())
            printAttributeDifferences();
    }

    private void init(ColumnDifference colDiff, MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
        this.colDiff = colDiff;
        this.expected = colDiff.getExpectedCol();
        this.actual = colDiff.getActualCol();
    }

    private void printAttributeDifferences() {
        printAttributeNameDiff();
        printAttributeDataTypeDiff();
        printAttributeDataLengthDiff();
        printAttributeDataPrecisionDiff();
        printAttributeDataScaleDiff();
        printAttributeDataDefaultDiff();
        printAttributeCharUsedDiff();
        printAttributeNullableDiff();
        printAttributeIdDiff();
    }

    private void printAttributeIdDiff() {
        if (colDiff.hasDifference(ColumnDiffType.ID))
            messagePrinter.printlnVV("ID (" + expected.getColumnID()
                                   + "," + actual.getColumnID() + ")");
    }

    private void printAttributeNullableDiff() {
        if (colDiff.hasDifference(ColumnDiffType.NULLABLE))
            messagePrinter.printlnVV("Nullable (" + expected.isNullable()
                    + "," + actual.isNullable() + ")");
    }

    private void printAttributeCharUsedDiff() {
        if (colDiff.hasDifference(ColumnDiffType.CHAR_USED))
            messagePrinter.printlnVV("Char used (" + expected.getCharUsed()
                    + "," + actual.getCharUsed() + ")");

    }

    private void printAttributeDataDefaultDiff() {
        if (colDiff.hasDifference(ColumnDiffType.DATA_DEFAULT))
            messagePrinter.printlnVV("Data default (" + expected.getDataDefault()
                    + "," + actual.getDataDefault() + ")");
    }

    private void printAttributeDataScaleDiff() {
        if (colDiff.hasDifference(ColumnDiffType.SCALE))
            messagePrinter.printlnVV("Data scale (" + expected.getDataScale()
                    + "," + actual.getDataScale() + ")");
    }

    private void printAttributeDataPrecisionDiff() {
        if (colDiff.hasDifference(ColumnDiffType.PRECISION))
            messagePrinter.printlnVV("Data precision (" + expected.getDataPrecision()
                    + "," + actual.getDataPrecision() + ")");

    }

    private void printAttributeDataLengthDiff() {
        if (colDiff.hasDifference(ColumnDiffType.LENGTH))
            messagePrinter.printlnVV("Data length (" + expected.getDataLength()
                    + "," + actual.getDataLength() + ")");
    }

    private void printAttributeDataTypeDiff() {
        if (colDiff.hasDifference(ColumnDiffType.TYPE))
            messagePrinter.printlnVV("Data type (" + expected.getType()
                    + "," + actual.getType() + ")");
    }

    private void printAttributeNameDiff() {
        if (colDiff.hasDifference(ColumnDiffType.NAME))
            messagePrinter.printlnVV("Name (" + expected.getName()
                    + "," + actual.getName() + ")");
    }

    private void printHeader() {
        if (colDiff.hasDifferences())
            messagePrinter.printlnVV("Column " + expected.getName() + " has those differences (snapshot, database):");
        else
            messagePrinter.printlnVV("Columns are equal.");
    }
}
