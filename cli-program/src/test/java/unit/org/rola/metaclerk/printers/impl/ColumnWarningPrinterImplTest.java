package org.rola.metaclerk.printers.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.Columns;
import org.rola.metaclerk.model.diff.ColumnDifference;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.test.BufferedSystemStreams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ColumnWarningPrinterImplTest {

    private BufferedSystemStreams systemStreams;
    private MessagePrinter printer;
    private ColumnWarningPrinterImpl colWarningPrinter;

    @Before

    public void setUp() throws Exception {
        systemStreams = new BufferedSystemStreams();
        printer = new MessagePrinterImpl(systemStreams.getSystemOut(), PrinterVerboseLevel.VERY_VERBOSE);
        colWarningPrinter = new ColumnWarningPrinterImpl();
    }

    @Test
    public void columnDifferenceWithoutWarning_print_doesntPrintAnything() throws Exception {
        ColumnDifference diff = Columns.createColumnPrecisionDifference();
        colWarningPrinter.print(diff, printer);

        assertTrue(systemStreams.getSystemOutBuffer().toString().length() == 0);
    }

    @Test
    public void columnDifferenceWithWarning_print_printsWarning() throws Exception {
        ColumnDifference diff = Columns.createColumnPrecisionDifferenceWithWarning("Warning message");
        colWarningPrinter.print(diff, printer);

        String buf = systemStreams.getSystemOutBuffer().toString();
        assertEquals("Warning:column(expected):Warning message", buf.trim());
    }
}