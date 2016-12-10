package org.rola.metaclerk.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class BufferedSystemStreams extends SystemStreams {
    private ByteArrayOutputStream outBuffer;
    private ByteArrayOutputStream errBuffer;
    private InputStream inStream;
    private PrintStream outStream;
    private PrintStream errStream;

    public BufferedSystemStreams() {
        clear();
    }

    public ByteArrayOutputStream getSystemErrBuffer() {
        return errBuffer;
    }

    public void setSystemInStream(InputStream inStream) {
        this.inStream = inStream;
    }

    public ByteArrayOutputStream getSystemOutBuffer() {
        return outBuffer;
    }

    @Override
    public InputStream getSystemIn() {
        return inStream;
    }

    @Override
    public PrintStream getSystemOut() {
        return outStream;
    }

    @Override
    public PrintStream getSystemErr() {
        return errStream;
    }

    public void clear() {
        outBuffer = new ByteArrayOutputStream();
        errBuffer = new ByteArrayOutputStream();

        errStream = new PrintStream(errBuffer);
        outStream = new PrintStream(outBuffer);

        inStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }
}
