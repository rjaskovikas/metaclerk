package org.rola.metaclerk.test;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class SystemStreamsTest {
    @Test
    public void systemStreamsObject_getMethods_retursSystemeStreams() throws Exception {
        SystemStreams streams = new SystemStreams();
        assertEquals(System.in, streams.getSystemIn());
        assertEquals(System.out, streams.getSystemOut());
        assertEquals(System.err, streams.getSystemErr());
    }

    @Test
    public void bufferedSystemsObject_getMethods_returnsNoSystemStreams() throws Exception {
        SystemStreams streams = new BufferedSystemStreams();
        assertNotEquals(null, streams.getSystemIn());
        assertNotEquals(System.out, streams.getSystemOut());
        assertNotEquals(System.err, streams.getSystemErr());
    }

    @Test
    public void bufferedSystemsObject_setInputStream_assignsNewInputStream() throws Exception {
        BufferedSystemStreams streams = new BufferedSystemStreams();
        InputStream buf = streams.getSystemIn();
        streams.setSystemInStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });

        assertNotEquals(buf, streams.getSystemIn());
    }
}