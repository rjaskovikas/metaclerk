package org.rola.metaclerk.test;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class StreamFactoryTest {

    private int createPrintStreamCallCount;
    private int createInputStreamCallCount;
    private InputStream inputStream;
    private StreamFactory factory;

    @Before
    public void setUp() throws Exception {
        setupStreamFactoryMock();
    }


    @Test
    public void StreamFactory_createPrintStream_returnPrintStream() throws Exception {
        assertEquals(System.out, factory.createPrintStream("aa"));
        assertEquals(1, createPrintStreamCallCount);
        assertEquals(0, createInputStreamCallCount);
    }

    @Test
    public void StreamFactory_createInputStream_returnInputStream() throws Exception {
        InputStream a = factory.createInputStream("aa");
        assertEquals(inputStream, a);
        assertEquals(1, createInputStreamCallCount);
        assertEquals(0, createPrintStreamCallCount);
    }

    private void setupStreamFactoryMock() {
        factory = new StreamFactory() {

            @Override
            public PrintStream createPrintStream(String fileName) throws FileNotFoundException {
                createPrintStreamCallCount++;
                return System.out;
            }

            @Override
            public InputStream createInputStream(String fileName) throws FileNotFoundException {
                createInputStreamCallCount++;
                return createInMemoryInputStream();
            }
        };
    }

    private InputStream createInMemoryInputStream() {
        inputStream = new ByteArrayInputStream("la la".getBytes());
        return inputStream;
    }
}