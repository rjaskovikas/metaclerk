package org.rola.metaclerk.printers.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.Columns;
import org.rola.metaclerk.model.Tables;
import org.rola.metaclerk.model.diff.TableDiffType;
import org.rola.metaclerk.model.diff.TableDifference;
import org.rola.metaclerk.printers.api.ColumnDifferencePrinter;
import org.rola.metaclerk.printers.api.MessagePrinter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class TableDifferencePrinterImplTest {

    private TableDifferencePrinterImpl printer;
    private MessagePrinter messagePrinter;
    private final List<String> messages = new ArrayList<>();
    private TableDifference tblDiff;

    @Before
    public void setUp() throws Exception {
        ColumnDifferencePrinter colPrinter = (colDiff, messagePrinter1) -> messages.add(colDiff.getDiffType().toString());
        printer = new TableDifferencePrinterImpl();
        printer.setColDiffPrinter(colPrinter);

        messagePrinter = new StringListMessagePrinterImpl(messages);
    }

    @Test(expected = NullPointerException.class)
    public void paramsAsNulls_print_throwsNullPointerException() throws Exception {

        printer.print(null, null);
    }

    @Test
    public void expectedNotFoundTableDifference_print_printsRightMessage() throws Exception {
        getTableDifference(TableDiffType.EXPECTED_NOT_FOUND);
        tblDiff.setActualTable(null);

        printer.print(tblDiff, messagePrinter);

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).contains("database"));
        assertTrue(messages.get(1).indexOf("---") == 0);
    }

    @Test
    public void actualNewTableDifference_print_printsRightMessage() throws Exception {
        getTableDifference(TableDiffType.ACTUAL_NEW);
        tblDiff.setExpectedTable(null);

        printer.print(tblDiff, messagePrinter);

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).contains("snapshot"));
        assertTrue(messages.get(1).indexOf("---") == 0);
    }

    @Test
    public void columnsTableDifference_print_printsRightMessage() throws Exception {
        getTableDifference(TableDiffType.COLUMNS);
        tblDiff.addColumnDifference(Columns.createColumnPrecisionDifference());

        printer.print(tblDiff, messagePrinter);

        assertEquals(3, messages.size());
        assertTrue(messages.get(0).contains("'expected' are different"));
        assertTrue(messages.get(1).contains("COLUMN"));
        assertTrue(messages.get(2).indexOf("---") == 0);
    }

    private void getTableDifference(TableDiffType difType) {
        tblDiff = Tables.createTableNameDifference();
        tblDiff.addTableDifference(difType);
    }
}