package org.rola.metaclerk.test;

import java.io.InputStream;
import java.io.PrintStream;


public class SystemStreams {

    private static final SystemStreams instance = new SystemStreams();

    SystemStreams() {}

    public InputStream getSystemIn() {
        return System.in;
    }

    public PrintStream getSystemOut() {
        return System.out;
    }

    public PrintStream getSystemErr() {
        return System.err;
    }

    public static SystemStreams getInstance() {
        return instance;
    }
}
