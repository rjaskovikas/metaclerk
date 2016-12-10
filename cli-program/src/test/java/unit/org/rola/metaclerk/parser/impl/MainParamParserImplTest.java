package org.rola.metaclerk.parser.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.parser.api.*;
import org.rola.metaclerk.test.BufferedSystemStreams;

public class MainParamParserImplTest {
    private int printProgamUsageCallCount;
    private String[] cmdParams;
    private int closeProgramCallCount;
    private MainParamParserImpl parser;
    private int createSnapshotParserCallCount;
    private int createCheckParserCallCount;
    private BufferedSystemStreams systemStreams;

    @Before
    public void setup() {
        systemStreams = new BufferedSystemStreams();

        SnapshotCmdParamsParserFactory snapshotParserFactory = () -> {
            createSnapshotParserCallCount++;
            return new SnapshotCmdParamsParser() {
                @Override
                public void closeProgram() {
                }

                @Override
                public void parseAndExecute(String[] params) {
                    cmdParams = params;
                }
            };
        };

        CheckCmdParamsParserFactory checkParserFactory = () -> {
            createCheckParserCallCount++;
            return new CheckCommandParamsParser() {
                @Override
                public void parseAndExecute(String[] params) {
                    cmdParams = params;
                }

                @Override
                public void closeProgram() {
                }
            };
        };

        parser = new MainParamParserImpl(checkParserFactory, snapshotParserFactory, systemStreams) {
            @Override
            protected void printProgramUsage() {
                MainParamParserImplTest.this.printProgamUsageCallCount++;
                super.printProgramUsage();
            }
        };
    }

    @Test
    public void emptyParamList_parse_printsProgramUsage() throws Exception {
        parser.parseAndExecute(new String[]{});

        assertEquals(1, this.printProgamUsageCallCount);
    }

    @Test
    public void badCommand_parse_printsProgramUsage() throws Exception {
        parser.parseAndExecute(new String[]{"non existing command"});

        assertEquals(1, this.printProgamUsageCallCount);
    }

    @Test
    public void snapshotCommand_parse_doesntPrintProgramUsage() throws Exception {
        parser.parseAndExecute(new String[]{MainParamParser.SNAPSHOT_CMD});

        assertEquals(0, this.printProgamUsageCallCount);
        assertEquals(1, createSnapshotParserCallCount);
    }

    @Test
    public void checkCommand_parse_doesntPrintProgramUsage() throws Exception {
        parser.parseAndExecute(new String[]{MainParamParser.CHECK_CMD});

        assertEquals(0, this.printProgamUsageCallCount);
        assertEquals(1, createCheckParserCallCount);
    }

    @Test
    public void helpParamAnyNumberOfParams_parse_printsProgramUsage() throws Exception {
        parser.parseAndExecute(new String[]{"--help"});
        assertEquals(1, this.printProgamUsageCallCount);
        assertTrue(systemStreams.getSystemErrBuffer().toString().contains("Program usage:"));

        printProgamUsageCallCount = 0;
        parser.parseAndExecute(new String[]{"--help", "aa"});
        assertEquals(1, this.printProgamUsageCallCount);

        printProgamUsageCallCount = 0;
        parser.parseAndExecute(new String[]{"--help", "aa", "bb"});
        assertEquals(1, this.printProgamUsageCallCount);

        printProgamUsageCallCount = 0;
        parser.parseAndExecute(new String[]{"--help", "aa", "bb", "cc"});
        assertEquals(1, this.printProgamUsageCallCount);
    }

    @Test
    public void snapshotCmdWithOneParams_parse_snapshotParserIsCalledWithEmptyParamsList() throws Exception {
        parser.parseAndExecute(new String[]{MainParamParser.SNAPSHOT_CMD});

        assertEquals(0, cmdParams.length);
    }

    @Test
    public void snapshotCmdWithTwoParams_parse_snapshotParserIsCalledWithOneParamInList() throws Exception {
        parser.parseAndExecute(new String[]{MainParamParser.SNAPSHOT_CMD, "param1"});

        assertEquals(1, cmdParams.length);
        assertEquals("param1", cmdParams[0]);
    }

    @Test
    public void checkCmdWithOneParams_parse_snapshotParserIsCalledWithEmptyParamsList() throws Exception {
        parser.parseAndExecute(new String[]{MainParamParser.CHECK_CMD});

        assertEquals(0, cmdParams.length);
    }

    @Test
    public void checkCmdWithTwoParams_parse_snapshotParserIsCalledWithOneParamInList() throws Exception {
        parser.parseAndExecute(new String[]{MainParamParser.CHECK_CMD, "param1"});

        assertEquals(1, cmdParams.length);
        assertEquals("param1", cmdParams[0]);
    }

    @Test
    public void commandParserIsNull_closeProgram_doesNothing() throws Exception {
        parser.cmdParser = null;
        parser.closeProgram(); // ok, if doesn't throw exception
    }

    @Test
    public void commandParser_closeProgram_callsCommandParserCloseProgram() throws Exception {
        closeProgramCallCount = 0;
        parser.cmdParser = new BaseCommandParamsParser() {
            @Override
            public void parseAndExecute(String[] params) {

            }

            @Override
            public void closeProgram() {
                closeProgramCallCount++;
            }
        };
        parser.closeProgram();
        assertEquals(1, closeProgramCallCount);
    }
}
