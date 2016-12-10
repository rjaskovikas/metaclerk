package org.rola.metaclerk.parser.impl;

import org.junit.Test;
import org.rola.metaclerk.command.api.CheckCommandFactory;
import org.rola.metaclerk.command.api.SnapshotCommandFactory;
import org.rola.metaclerk.command.impl.CheckCommandImpl;
import org.rola.metaclerk.command.impl.SnapshotCommandImpl;
import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.DbSchemas;
import org.rola.metaclerk.model.Tables;
import org.rola.metaclerk.parser.api.CheckCmdParamsParserFactory;
import org.rola.metaclerk.parser.api.SnapshotCmdParamsParserFactory;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.printers.impl.MessagePrinterImpl;
import org.rola.metaclerk.printers.impl.SchemaDifferencePrinterImpl;
import org.rola.metaclerk.test.BufferedSystemStreams;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.TestExitManager;
import org.rola.metaclerk.xml.impl.SchemaXmlReaderImpl;
import org.rola.metaclerk.xml.impl.SchemaXmlWriterImpl;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FullMainParserCompTest {
    private MainParamParserImpl mainParser;

    private final BufferedSystemStreams systemStreams = new BufferedSystemStreams();
    private MessagePrinter messagePrinter = new MessagePrinterImpl(systemStreams.getSystemOut(), PrinterVerboseLevel.NONE);
    private TestExitManager exitManager = new TestExitManager();

    @Test
    public void mainParserWithCheckCommandArgument_parse_callCheckCommandWithCorrectParams() throws Exception {
        CheckCommandFactory checkCommandFactory = () -> {

            AllSchemaObjectsDAO dao = new AllSchemaObjectsDAO() {
                @Override
                public void initDAO(String jdbcConnectionStr, String dbUser, String dbPassword) {
                    assertEquals("jdbc_str", jdbcConnectionStr);
                    assertEquals("db_user", dbUser);
                    assertEquals("db_password", dbPassword);
                }

                @Override
                public void closeDAO() {}

                @Override
                public DbSchema getAllSchemaObjects(String schemaName) {
                    assertEquals("db_schema", schemaName);
                    DbSchema sc = DbSchemas.createDbSchema(schemaName);
                    sc.getTables().add(Tables.createTableWithColumns("ignore1", "ignore"));
                    return sc;
                }
            };

            StreamFactory streamFactory = new StreamFactory() {
                @Override
                public InputStream createInputStream(String fileName) throws FileNotFoundException {
                    if (fileName.equalsIgnoreCase("IgnoreFileName"))
                        return DbSchemas.createIgnoreListStream();
                    return super.createInputStream(fileName);
                }
            };

            DynamicDAOFactoryLoader daoFactoryLoader = (dbType) -> {
                return ()-> {return dao;};
            };


            return new CheckCommandImpl(daoFactoryLoader, new SchemaXmlReaderImpl(),
                    new SchemaDifferencePrinterImpl(), streamFactory, systemStreams);
        };

        systemStreams.setSystemInStream(DbSchemas.createDbSchemaInputStream()); // read db schema snapshot from system input

        CheckCmdParamsParserFactory parserFactory = () ->
                new CheckCmdParamsParserImp(messagePrinter, checkCommandFactory, systemStreams, exitManager);

        mainParser = new MainParamParserImpl(parserFactory, null, systemStreams);

        mainParser.parseAndExecute(new String[]{"check", "-v",
                "-c", "jdbc_str",
                "-u", "db_user",
                "-p", "db_password",
                "-s", "db_schema",
                "-il", "IgnoreFileName"});

        mainParser.closeProgram();

        assertEquals(0, exitManager.getExitCode());
    }

    @Test
    public void mainParserWithSnapshotCommandArgument_parse_callSnapshotCommandWithCorrectParams() throws Exception {

        SnapshotCommandFactory cmdFactory = () -> {
            AllSchemaObjectsDAO dao = new AllSchemaObjectsDAO() {
                @Override
                public DbSchema getAllSchemaObjects(String schemaName) {
                    assertEquals("db_schema", schemaName);
                    return DbSchemas.createDbSchema(schemaName);
                }

                @Override
                public void initDAO(String jdbcConnectionStr, String dbUser, String dbPassword) {
                    assertEquals("jdbc_str", jdbcConnectionStr );
                    assertEquals("db_user", dbUser );
                    assertEquals("db_password", dbPassword);
                }

                @Override
                public void closeDAO() {}
            };

            DynamicDAOFactoryLoader daoFactoryLoader = (dbType) -> {
                return ()-> {return dao;};
            };

            return new SnapshotCommandImpl(daoFactoryLoader, new SchemaXmlWriterImpl(), null, systemStreams);
        };

        SnapshotCmdParamsParserFactory parserFactory = () ->
                new SnapshotCmdParamsParserImpl(cmdFactory, StreamFactory.getInstance(), systemStreams, exitManager);
        mainParser = new MainParamParserImpl(null, parserFactory, systemStreams);

        mainParser.parseAndExecute(new String[]{"snapshot", "-v",
                "-c", "jdbc_str",
                "-u", "db_user",
                "-p", "db_password",
                "-s", "db_schema"});

        assertTrue(systemStreams.getSystemOutBuffer().toString().contains("name=\"tableVienas\""));
        assertTrue(systemStreams.getSystemOutBuffer().toString().contains("name=\"tableDu\""));
    }

    @Test
    public void snapshotCommandWithMissingDbUserPasswordArgument_parse_printCommandUsage() throws Exception {
        mainParser = new MainParamParserImpl(null, this::createSnapshotParser, systemStreams);

        mainParser.parseAndExecute(new String[]{"snapshot", "-v",
                "-c", "jdbc_str",
                "-u", "db_user",
                "-s", "db_schema"});

        assertTrue(systemStreams.getSystemErrBuffer().toString().contains("user password is missing"));
        assertTrue(systemStreams.getSystemErrBuffer().toString().contains("Command usage"));
    }

     private SnapshotCmdParamsParserImpl createSnapshotParser() {
        return new SnapshotCmdParamsParserImpl(
                SnapshotCommandFactory.createFactory(),
                StreamFactory.getInstance(),
                systemStreams, exitManager);
    }
}
