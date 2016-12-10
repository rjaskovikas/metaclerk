package org.rola.metaclerk.test;

import java.io.*;

public class StreamFactory {

    private static StreamFactory instance = new StreamFactory();

    protected StreamFactory() {
    }

    public static StreamFactory getInstance() {
        return instance;
    }

    public PrintStream createPrintStream(String fileName) throws FileNotFoundException {
        return new PrintStream(new File(fileName));
    }

    public InputStream createInputStream(String fileName) throws FileNotFoundException {
        return new FileInputStream(fileName);
    }
}
