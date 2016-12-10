package component.org.rola.db.comparator.parser.impl;

import org.junit.Test;
import org.rola.metaclerk.command.api.CheckCommand;
import org.rola.metaclerk.command.api.CheckCommandFactory;
import org.rola.metaclerk.command.api.SnapshotCommandFactory;
import org.rola.metaclerk.model.DbSchemas;
import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.parser.api.CheckCmdParamsParserFactory;
import org.rola.metaclerk.parser.api.SnapshotCmdParamsParserFactory;
import org.rola.metaclerk.parser.impl.CheckCmdParamsParserImp;
import org.rola.metaclerk.parser.impl.MainParamParserImpl;
import org.rola.metaclerk.parser.impl.SnapshotCmdParamsParserImpl;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.printers.impl.MessagePrinterImpl;
import org.rola.metaclerk.test.BufferedSystemStreams;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.TestExitManager;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainParamParserImplCompTest {

    private static final String IGNORE_LIST_FILE_NAME =  "cc:ignoreFileName";
    private MainParamParserImpl mainParser ;
    private InputStream ignoreListStream;
    private final BufferedSystemStreams systemStreams = new BufferedSystemStreams();
    private StreamFactory streamFactory;
    private TestExitManager exitManager = new TestExitManager();

    @Test
    public void mainParserWithCheckCommandArgument_parse_callCheckCommandWithCorrectParams() throws Exception {
        streamFactory = new StreamFactory() {
            @Override
            public InputStream createInputStream(String fileName) throws FileNotFoundException {
                if (fileName.equalsIgnoreCase(IGNORE_LIST_FILE_NAME)) {
                    ignoreListStream = DbSchemas.createIgnoreListStream();
                    return ignoreListStream;
                }
                throw new FileNotFoundException(fileName);
            }
        };

        CheckCommandFactory f =  () -> {


                return new CheckCommand() {
                    @Override
                    public void execute(JdbcConnectionParam param, String schemaName, String inputFileName, String ignoreListFileName,
                                        PrinterVerboseLevel verboseLevel) {
                        assertEquals("jdbc_str", param.getJdbcConnectionStr());
                        assertEquals("db_user", param.getUserName());
                        assertEquals("db_password", param.getPassword());
                        assertEquals("db_schema", schemaName);
                        assertEquals("oracle", param.getDbType());
                        assertNull(inputFileName);
                        assertEquals(PrinterVerboseLevel.VERBOSE, verboseLevel);
                        assertEquals(MainParamParserImplCompTest.this.ignoreListStream, ignoreListStream);
                    }

                    @Override
                    public boolean isSchemasEqual() {
                        return true;
                    }
                };
        };

        CheckCmdParamsParserFactory checkParserFactory = () ->
                new CheckCmdParamsParserImp(MessagePrinterImpl.getDefaultPrinter(), f, systemStreams, exitManager);

        mainParser = new MainParamParserImpl(checkParserFactory, null, systemStreams);

        mainParser.parseAndExecute(new String[] {"check", "-v",
                                        "-c", "jdbc_str",
                                        "-u", "db_user",
                                        "-p", "db_password",
                                        "-s", "db_schema",
                                        "-il", IGNORE_LIST_FILE_NAME});
        mainParser.closeProgram();

        assertEquals(0, exitManager.getExitCode());
    }

    @Test
    public void mainParserWithSnapshotCommandArgument_parse_callSnapshotCommandWithCorrectParams() throws Exception {
        SnapshotCommandFactory cmdFactory = () -> {
            return (params, schemaName, stream) ->  {
                assertEquals("db_schema", schemaName);
                assertEquals("jdbc_str", params.getJdbcConnectionStr());
                assertEquals("db_user", params.getUserName());
                assertEquals("db_password", params.getPassword());
                assertEquals("testDB", params.getDbType());
            };
        };

        SnapshotCmdParamsParserFactory parserFactory = () ->
                new SnapshotCmdParamsParserImpl(cmdFactory, StreamFactory.getInstance(), systemStreams, exitManager);

        mainParser = new MainParamParserImpl(null, parserFactory, systemStreams);

        mainParser.parseAndExecute(new String[] {"snapshot", "-v",
                "-c", "jdbc_str",
                "-u", "db_user",
                "-p", "db_password",
                "-s", "db_schema",
                "-d", "testDB"});
    }

    @Test
    public void snapshotCommandWithMissingDbUserPasswordArgument_parse_printCommandUsage() throws Exception {

        mainParser = new MainParamParserImpl(null, this::createSnapshotParser, systemStreams);

        mainParser.parseAndExecute(new String[] {"snapshot", "-v",
                "-c", "jdbc_str",
                "-u", "db_user",
                "-s", "db_schema"});

        assertTrue(systemStreams.getSystemErrBuffer().toString().contains("password"));
        assertTrue(systemStreams.getSystemErrBuffer().toString().contains("Command usage"));
    }

    private SnapshotCmdParamsParserImpl createSnapshotParser() {
        return new SnapshotCmdParamsParserImpl(
                SnapshotCommandFactory.createFactory(),
                StreamFactory.getInstance(),
                systemStreams, exitManager);
    }

}