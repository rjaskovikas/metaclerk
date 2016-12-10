package org.rola.metaclerk.printers.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.Columns;
import org.rola.metaclerk.model.Tables;
import org.rola.metaclerk.model.diff.SchemaDifference;
import org.rola.metaclerk.model.diff.TableDifference;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.xml.impl.Privileges;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaDifferencePrinterImplTest {

    private final List<String> messages = new ArrayList<>();
    private SchemaDifferencePrinterImpl printer;
    private MessagePrinter messagePrinter;

    @Before
    public void setUp() throws Exception {
        printer = new SchemaDifferencePrinterImpl();

        messagePrinter = new StringListMessagePrinterImpl(messages);
    }

    @Test (expected = NullPointerException.class)
    public void nullSchemaDifference_print_throwsNullPointerException() throws Exception {
        printer.print(null, null);
    }

    @Test
    public void schemaDifference_print_printsDifference() throws Exception {
        SchemaDifference shd = new SchemaDifference();
        shd.addTableDifference(Tables.createTableNameDifference());
        printer.print(shd, messagePrinter);

        assertEquals(2, messages.size());
        assertEquals("Tables 'expected' are different.", messages.get(0));
        assertEquals("-----------", messages.get(1));
    }

    @Test
    public void schemaDiffWithPrivilegeDiff_print_printsSchemaDiff() throws Exception {
        SchemaDifference shd = new SchemaDifference();
        shd.addPrivilegeDifference(Privileges.createPrivilegeDifference());
        printer.print(shd, messagePrinter);

//        StringListMessagePrinterImpl.printToSystemOut(messages);
        assertEquals("Different grant option for (grant select on owner.name) (snapshot, database)", messages.get(0));
        assertEquals("YES, NO", messages.get(1));
        assertEquals("-----------", messages.get(2));
    }

    @Test
    public void schemaDifferenceWithColumnWarning_print_printsDifference() throws Exception {
        SchemaDifference shd = new SchemaDifference();
        TableDifference dif = Tables.createTableNameDifference();
        dif.addColumnDifference(Columns.createColumnPrecisionDifferenceWithWarning("test warning"));
        shd.addTableDifference(dif);
        printer.print(shd, messagePrinter);

//        StringListMessagePrinterImpl.printToSystemOut(messages);
        assertEquals(7, messages.size());
        assertEquals("Tables 'expected' are different.", messages.get(0));
        assertEquals("Column expected has those differences (snapshot, database):", messages.get(1));
        assertEquals("Data precision (2,2)", messages.get(2));
        assertEquals("-----------", messages.get(3));
        assertEquals("Table/view (expected) has warnings:", messages.get(4));
        assertEquals("Warning:column(expected):test warning", messages.get(5));
        assertEquals("-----------", messages.get(6));
    }

}
