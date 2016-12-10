package org.rola.metaclerk.printers.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.Columns;
import org.rola.metaclerk.model.diff.ColumnDiffType;
import org.rola.metaclerk.model.diff.ColumnDifference;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class ColumnDifferencePrinterImplTest {
    private MessagePrinter mesPrinter;
    private final List<String> printMessages = new ArrayList<>();
    private ColumnDescription a;
    private ColumnDescription b;
    private ColumnDifference colDiff;

    @Before
    public void setUp() throws Exception {
        mesPrinter = new StringListMessagePrinterImpl(printMessages);
    }

    @Test(expected = NullPointerException.class)
    public void nullsAsParameters_print_throwsNullPointerException() throws Exception {
        ColumnDifferencePrinterImpl printer = new ColumnDifferencePrinterImpl();
        printer.print(null, null);
    }

    @Test
    public void equalColumns_print_printsColumnsEquals() throws Exception {
        prepareColumnsAndDiff();

        print();

        assertEquals(1, printMessages.size());
        assertTrue(printMessages.get(0).contains("equal"));
    }

    @Test
    public void diffColumnName_print_printsNameIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setName("du");
        colDiff.addColumnDiff(ColumnDiffType.NAME);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(vienas,du)") > 0);
    }

    @Test
    public void diffColumnType_print_printsTypeIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setType("du");
        colDiff.addColumnDiff(ColumnDiffType.TYPE);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(NUMBER,du)") > 0);
    }

    @Test
    public void diffColumnLength_print_printsLengthIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setDataLength(55);
        colDiff.addColumnDiff(ColumnDiffType.LENGTH);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(5,55)") > 0);
    }

    @Test
    public void diffColumnPrecision_print_printsPrecisionIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setDataPrecision(54);
        colDiff.addColumnDiff(ColumnDiffType.PRECISION);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(2,54)") > 0);
    }

    @Test
    public void diffColumnScale_print_printsScaleIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setDataScale(53);
        colDiff.addColumnDiff(ColumnDiffType.SCALE);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(6,53)") > 0);
    }

    @Test
    public void diffColumnDataDefault_print_printsDataDefaultIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setDataDefault("uuu");
        colDiff.addColumnDiff(ColumnDiffType.DATA_DEFAULT);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(15,uuu)") > 0);
    }

    @Test
    public void diffColumnCharUsed_print_printsCharUsedIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setCharUsed("ccc");
        colDiff.addColumnDiff(ColumnDiffType.CHAR_USED);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(B,ccc)") > 0);
    }

    @Test
    public void diffColumnId_print_printsColumnIdIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setColumnID(3);
        colDiff.addColumnDiff(ColumnDiffType.ID);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(1,3)") > 0);
    }

    @Test
    public void diffColumnNullable_print_printsColumnNullableIsDiff() throws Exception {
        prepareColumnsAndDiff();
        b.setNullable(true);
        colDiff.addColumnDiff(ColumnDiffType.NULLABLE);

        print();

        assertEquals(2, printMessages.size());
        assertTrue(printMessages.get(0).contains("differences"));
        assertTrue(printMessages.get(1).indexOf("(false,true)") > 0);
    }

    @Test
    public void newActualColumn_print_printsNewColumnDiff() throws Exception {
        a = Columns.createNUMBERColumn(1, "vienas");
        colDiff = new ColumnDifference();
        colDiff.setActualCol(a);
        colDiff.addColumnDiff(ColumnDiffType.ACTUAL_NEW);

        print();

        assertEquals(1, printMessages.size());
        assertTrue(printMessages.get(0).indexOf("snapshot") > 0);
    }

    @Test
    public void expectedNotFound_print_printsExpectedColumnDIff() throws Exception {
        a = Columns.createNUMBERColumn(1, "vienas");
        colDiff = new ColumnDifference();
        colDiff.setExpectedCol(a);
        colDiff.addColumnDiff(ColumnDiffType.EXPECTED_NOT_FOUND);

        print();

        assertEquals(1, printMessages.size());
        assertTrue(printMessages.get(0).indexOf("database") > 0);
    }

    private void print() {
        new ColumnDifferencePrinterImpl().print(colDiff, mesPrinter);
    }

    private void prepareColumnsAndDiff() {
        a = Columns.createNUMBERColumn(1, "vienas");
        b = Columns.createNUMBERColumn(1, "vienas");
        colDiff = new ColumnDifference();
        colDiff.setExpectedCol(a);
        colDiff.setActualCol(b);
    }
}