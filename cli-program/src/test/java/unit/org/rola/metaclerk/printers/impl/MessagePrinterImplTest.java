package org.rola.metaclerk.printers.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MessagePrinterImplTest {

    private MessagePrinter printer;
    private ByteArrayOutputStream byteStream;

    @Before
    public void setUp() throws Exception {
        byteStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStream);
        printer = new MessagePrinterImpl(printStream, PrinterVerboseLevel.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void nullPrintStream_println_throwsNullPointerException() throws Exception {
        printer = new MessagePrinterImpl(null, PrinterVerboseLevel.NONE);
        printer.println("test");
    }

    @Test
    public void noneVerbosityLevel_println_prints() throws Exception {
        printer.println("test");
        assertEquals(6, byteStream.toString().length());
    }

    @Test
    public void verboseVerbosityLevel_println_prints() throws Exception {
        printer.setVerboseLevel(PrinterVerboseLevel.VERBOSE);
        printer.println("test");
        assertEquals(6, byteStream.toString().length());
    }

    @Test
    public void veryVerboseVerbosityLevel_println_prints() throws Exception {
        printer.setVerboseLevel(PrinterVerboseLevel.VERY_VERBOSE);
        printer.println("test");
        assertEquals(6, byteStream.toString().length());
    }

    @Test
    public void noneVerbosityLevel_printlnV_doesntPrint() throws Exception {
        printer.printlnV("test");
        assertEquals(0, byteStream.toString().length());
    }

    @Test
    public void verboseVerbosityLevel_printlnV_prints() throws Exception {
        printer.setVerboseLevel(PrinterVerboseLevel.VERBOSE);
        printer.printlnV("test");
        assertEquals(6, byteStream.toString().length());
    }

    @Test
    public void veryVerboseVerbosityLevel_printlnV_prints() throws Exception {
        printer.setVerboseLevel(PrinterVerboseLevel.VERY_VERBOSE);
        printer.printlnV("test");
        assertEquals(6, byteStream.toString().length());
    }

    @Test
    public void noneVerbosityLevel_printlnVV_doesntPrint() throws Exception {
        printer.printlnVV("test");
        assertEquals(0, byteStream.toString().length());
    }

    @Test
    public void verboseVerbosityLevel_printlnVV_dosntPrint() throws Exception {
        printer.setVerboseLevel(PrinterVerboseLevel.VERBOSE);
        printer.printlnVV("test");
        assertEquals(0, byteStream.toString().length());
    }

    @Test
    public void veryVerboseVerbosityLevel_printlnVV_prints() throws Exception {
        printer.setVerboseLevel(PrinterVerboseLevel.VERY_VERBOSE);
        printer.printlnVV("test");
        assertEquals(6, byteStream.toString().length());
    }

    @Test
    public void verboseVerbosityLevel_setVerboseLevel_changesVerbosityLevel() throws Exception {
        assertEquals(PrinterVerboseLevel.NONE, printer.getVerboseLevel());
        printer.setVerboseLevel(PrinterVerboseLevel.VERBOSE);
        assertEquals(PrinterVerboseLevel.VERBOSE, printer.getVerboseLevel());
    }
}