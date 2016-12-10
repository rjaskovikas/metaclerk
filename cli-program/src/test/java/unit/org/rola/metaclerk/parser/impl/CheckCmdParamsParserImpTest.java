package org.rola.metaclerk.parser.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.command.api.CheckCommand;
import org.rola.metaclerk.command.api.CheckCommandFactory;
import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.parser.api.JdbcConnectionParamsParser;
import org.rola.metaclerk.parser.api.VerboseLevelParser;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.printers.impl.MessagePrinterImpl;
import org.rola.metaclerk.test.BufferedSystemStreams;
import org.rola.metaclerk.test.StringUtils;
import org.rola.metaclerk.test.TestExitManager;
import org.rola.metaclerk.utils.ArrayIterator;

import static org.junit.Assert.*;

public class CheckCmdParamsParserImpTest implements CheckCommand {
    private boolean throwJdbcParserCheckParamsException;
    private int executeCommandCallCount;
    private boolean callRealStreamFactory;
    private boolean schemaEqualReturnValue;

    private CheckCmdParamsParserImp parser;
    private VerboseLevelParser verboseParser;
    private JdbcConnectionParamsParser jdbcParser;
    private String executeInputFileName;
    private String executeIgnoreFileName;
    private BufferedSystemStreams systemStreams;
    private TestExitManager exitManager;
    private PrinterVerboseLevel executeVerboseLevel;


    @Before
    public void setup() {
        createEmbeddedParsers();
        createSystemStreams();
        CheckCommandFactory checkCommandFactory = () -> CheckCmdParamsParserImpTest.this;
        exitManager =  new TestExitManager();

        parser = new CheckCmdParamsParserImp(MessagePrinterImpl.getDefaultPrinter(),
                checkCommandFactory, systemStreams, exitManager);
        parser.setJdbcParser(jdbcParser);
    }

    private void createSystemStreams() {
        systemStreams = new BufferedSystemStreams();
    }


    private void createEmbeddedParsers() {
        jdbcParser = new JdbcConnectionParamsParser() {
            @Override
            public String getParamsDescriptionString() {
                return null;
            }

            @Override
            public String getParamsUsageString() {
                return null;
            }

            @Override
            public void setup(ArrayIterator<String> paramIt) {
            }

            @Override
            public boolean parseParam() {
                return false; //always return not my param
            }

            @Override
            public void checkMandatoryParams() {
                if (throwJdbcParserCheckParamsException)
                    throw new BadParamException("Missing par");
            }

            @Override
            public String getJdbcConnectionStr() {
                return null;
            }

            @Override
            public String getSchemaName() {
                return null;
            }

            @Override
            public String getDbPassword() {
                return null;
            }

            @Override
            public String getDbUser() {
                return null;
            }

            @Override
            public String getDbType() {
                return null;
            }

            @Override
            public JdbcConnectionParam getJdbcConnectionParam() {
                return null;
            }
        };
    }

    @Test
    public void emptyParamsList_parse_printsUsage() throws Exception {
        throwJdbcParserCheckParamsException = true; // imitate missing required jdbc values
        parser.parseAndExecute(new String[]{});

        assertEquals(0, executeCommandCallCount);
        assertEquals(1, getPrintCommandUsageCallCount());
        assertTrue(systemStreams.getSystemErrBuffer().toString().contains("metaclerk"));
    }

    @Test
    public void unknownParam_parse_printsErrorAndUsage() throws Exception {
        parser.parseAndExecute(new String[]{"-unknown"});

        assertEquals(1, getPrintCommandUsageCallCount());
        assertEquals(0, executeCommandCallCount);
    }

    @Test
    public void paramWithNoValue_parse_printsErrorAndUsage() throws Exception {
        parser.parseAndExecute(new String[]{"-i"});

        assertEquals(1, getPrintCommandUsageCallCount());
        assertEquals(0, executeCommandCallCount);
    }

    @Test
    public void allMandatoryAndOptionalParams_parse_executesCommand() throws Exception {
        parser.parseAndExecute(new String[]{"-i", "inFile", "-il", "ignFile"});

        assertEquals(0, getPrintCommandUsageCallCount());
        assertEquals("inFile", executeInputFileName);
        assertEquals("ignFile", executeIgnoreFileName);
        assertEquals(1, executeCommandCallCount);
    }

    @Test
    public void verboseLevelParams_parse_executeWithVerboseLevel() throws Exception {
        parser.parseAndExecute(new String[]{"-v"});

        assertEquals(0, getPrintCommandUsageCallCount());
        assertEquals(1, executeCommandCallCount);
        assertEquals(PrinterVerboseLevel.VERBOSE, executeVerboseLevel);
    }

    @Test
    public void veryVerboseLevelParams_parse_executeWithVeryVerboseLevel() throws Exception {
        parser.parseAndExecute(new String[]{"-vv"});

        assertEquals(0, getPrintCommandUsageCallCount());
        assertEquals(1, executeCommandCallCount);
        assertEquals(PrinterVerboseLevel.VERY_VERBOSE, executeVerboseLevel);
    }

    @Test
    public void helpArgument_parse_printsCommandUsage() throws Exception {
        throwJdbcParserCheckParamsException = true; // imitate missing jdbc params
        parser.parseAndExecute(new String[]{"--help"});
        assertEquals(1, getPrintCommandUsageCallCount());
        assertEquals(0, executeCommandCallCount);

        systemStreams.clear();
        parser.parseAndExecute(new String[]{"--help", "-c"});
        assertEquals(1, getPrintCommandUsageCallCount());
        assertEquals(0, executeCommandCallCount);

        systemStreams.clear();
        parser.parseAndExecute(new String[]{"--help", "-u", "bb"});
        assertEquals(1, getPrintCommandUsageCallCount());
        assertEquals(0, executeCommandCallCount);
    }

    @Test
    public void parserObject_getParamsDescriptionString_returnsDescriptionWithCheckWord() throws Exception {
        assertTrue(parser.getParamsDescriptionString().contains("check"));
    }

    @Test
    public void parserObject_getParamsUsageString_returnsDescriptionsWithParameters() throws Exception {
        assertTrue(parser.getParamsUsageString().contains("-i"));
        assertTrue(parser.getParamsUsageString().contains("-il"));
    }


    @Test
    public void inputFileNameIsNull_parse_usesStdin() throws Exception {
        callRealStreamFactory = true;
        parser.parseAndExecute(new String[]{});

        assertEquals(0, getPrintCommandUsageCallCount());
        assertEquals(1, executeCommandCallCount);
        assertNull(executeInputFileName);
    }

    @Test
    public void noIgnoreListFileName_parse_ignoreListStreamIsNull() throws Exception {
        callRealStreamFactory = true;
        parser.parseAndExecute(new String[]{});

        assertEquals(0, getPrintCommandUsageCallCount());
        assertEquals(1, executeCommandCallCount);
        assertNull(executeIgnoreFileName);
    }

    @Test
    public void equalSchemas_systemExit_callsDbComparatorSystemExitWith0() throws Exception {
        schemaEqualReturnValue = true;
        parser.parseAndExecute(new String[]{}); // creates command
        parser.closeProgram();
        assertEquals(0, exitManager.getExitCode());
    }

    @Override
    public void execute(JdbcConnectionParam jdbcParams, String schemaName, String inputFileName, String ignoreListFileName, PrinterVerboseLevel verboseLevel) {
        executeCommandCallCount++;
        executeInputFileName = inputFileName;
        executeIgnoreFileName = ignoreListFileName;
        executeVerboseLevel = verboseLevel;
    }

    @Override
    public boolean isSchemasEqual() {
        return schemaEqualReturnValue;
    }

    public int getPrintCommandUsageCallCount() {
        return StringUtils.getWordRepeatCount(systemStreams.getSystemErrBuffer().toString(), "Command usage:");
    }
}