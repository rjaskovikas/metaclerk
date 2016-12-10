package org.rola.metaclerk;

import org.junit.Test;
import org.rola.metaclerk.parser.api.MainParamParser;
import org.rola.metaclerk.MetaClerk;

import static org.junit.Assert.*;

public class MetaClerkTest extends MetaClerk {
    private static int closeProgramCallCount;
    private String[] passedParams;

    @Test
    public void someArgs_main_callsMainParserWithGivenArgs() throws Exception {
        run(new String[] {"check", "arguments"});

        assertEquals(2, passedParams.length);
        assertEquals("check", passedParams[0]);
        assertEquals("arguments", passedParams[1]);
        assertEquals(1, closeProgramCallCount);
    }

    @Override
    protected void createMainParamParser() {
        mainParser = new MainParamParser() {
            @Override
            public void parseAndExecute(String[] params) {
                MetaClerkTest.this.passedParams = params;
            }

            @Override
            public void closeProgram() {
                MetaClerkTest.closeProgramCallCount++;
            }
        };
    }
}