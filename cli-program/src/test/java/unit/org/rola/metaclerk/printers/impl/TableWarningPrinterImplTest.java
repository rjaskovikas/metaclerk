package org.rola.metaclerk.printers.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.Columns;
import org.rola.metaclerk.model.Tables;
import org.rola.metaclerk.model.diff.TableDifference;
import org.rola.metaclerk.printers.api.MessagePrinter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TableWarningPrinterImplTest {
    private MessagePrinter printer;
    private TableWarningPrinterImpl tableWarningPrinter;
    private List<String> messages = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        printer = new StringListMessagePrinterImpl(messages);
        tableWarningPrinter = new TableWarningPrinterImpl();
    }

    @Test
    public void tableDiffWithoutWarning_print_doesntPrintAnything() throws Exception {
        TableDifference dif = Tables.createTableNameDifference();

        tableWarningPrinter.print(dif, printer);

        assertTrue(messages.size() == 0);
    }

    @Test
    public void tableDiffWithWarning_print_doesntPrintAnything() throws Exception {
        TableDifference dif = Tables.createTableNameDifference();
        dif.addColumnDifference(Columns.createColumnPrecisionDifferenceWithWarning("Test warning"));

        tableWarningPrinter.print(dif, printer);

//        StringListMessagePrinterImpl.printToSystemOut(messages);
        assertEquals(3, messages.size());
        assertEquals("Table/view (expected) has warnings:", messages.get(0));
        assertEquals("Warning:column(expected):Test warning", messages.get(1));
        assertEquals("-----------", messages.get(2));
    }
}