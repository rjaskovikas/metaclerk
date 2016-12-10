package org.rola.metaclerk.printers.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.diff.PrivilegeDiffList;
import org.rola.metaclerk.model.diff.PrivilegeDiffType;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrivilegeDifferencePrinter;
import org.rola.metaclerk.xml.impl.Privileges;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PrivilegeDifferencePrinterImplTest {
    private final PrivilegeDifferencePrinter printer = new PrivilegeDifferencePrinterImpl();
    private MessagePrinter messagePrinter;
    private final List<String> messages = new ArrayList<>();
    private PrivilegeDiffList privDiffList;

    @Before
    public void setUp() throws Exception {
        messagePrinter = new StringListMessagePrinterImpl(messages);
    }

    @Test(expected = NullPointerException.class)
    public void paramsAsNulls_print_throwsNullPointerException() throws Exception {
        printer.print(null, null);
    }

    @Test
    public void expectedNotFoundDiff_print_printsCorrectMessage() throws Exception {
        createPrivilegeDiff(PrivilegeDiffType.EXPECTED_NOT_FOUNT);
        privDiffList.get(0).setActual(null);
        printer.print(privDiffList, messagePrinter);

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).contains("not found in database schema"));
    }

    @Test
    public void actualNewDiff_print_printsCorrectMessage() throws Exception {
        createPrivilegeDiff(PrivilegeDiffType.ACTUAL_NEW);
        privDiffList.get(0).setExpected(null);
        printer.print(privDiffList, messagePrinter);

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).contains("is new in database schema"));
    }

    @Test
    public void grantDiff_print_printsCorrectMessage() throws Exception {
        createPrivilegeDiff(PrivilegeDiffType.GRANT);
        printer.print(privDiffList, messagePrinter);

        assertEquals(3, messages.size());
        assertTrue(messages.get(0).contains("Different grant option for"));
        assertTrue(messages.get(0).contains(privDiffList.get(0).getActual().getName()));
        assertTrue(messages.get(0).contains(privDiffList.get(0).getActual().getOwnerName()));
        assertTrue(messages.get(0).contains(privDiffList.get(0).getActual().getPrivilege()));
    }

    private void createPrivilegeDiff(PrivilegeDiffType diffType) {
        privDiffList = new PrivilegeDiffList();
        privDiffList.add(Privileges.createPrivilegeDifference());
        privDiffList.get(0).setDifference(diffType);
    }
}