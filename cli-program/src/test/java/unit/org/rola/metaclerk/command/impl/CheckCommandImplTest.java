package org.rola.metaclerk.command.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.exception.ExecuteCommandException;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.DbSchemas;
import org.rola.metaclerk.model.Tables;
import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.printers.impl.MessagePrinterImpl;
import org.rola.metaclerk.printers.impl.SchemaDifferencePrinterImpl;
import org.rola.metaclerk.test.BufferedSystemStreams;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.SystemStreams;
import org.rola.metaclerk.xml.api.SchemaXmlReader;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CheckCommandImplTest {

    private static final String DAOError = "Bad JDBC connection";
    private CheckCommandImpl cmd;
    private DbSchema dbSchema;
    private boolean throwExceptionOnDaoOperation;
    private DbSchema xmlSchema;
    private JdbcConnectionParam jdbcParams;
    private int initDAOCallCount;
    private int closeDAOCallCount;


    @Before
    public void setUp() throws Exception {
        jdbcParams = new JdbcConnectionParam("con", "user", "pwd", "dbType");
        AllSchemaObjectsDAO dao = new AllSchemaObjectsDAO() {
            @Override
            public DbSchema getAllSchemaObjects(String schemaName) {
                if (throwExceptionOnDaoOperation)
                    throw new DAOException(DAOError, null);
                return dbSchema;
            }

            @Override
            public void initDAO(String jdbcConnectionStr, String dbUser, String dbPassword) {
                CheckCommandImplTest.this.initDAOCallCount++;
            }

            @Override
            public void closeDAO() {
                CheckCommandImplTest.this.closeDAOCallCount++;
            }
        };

        DynamicDAOFactoryLoader daoFactoryLoader = (dbType) -> {
            return ()-> {return dao;};
        };

        SchemaXmlReader xmlReader = stream -> xmlSchema;

        MessagePrinter printer = new MessagePrinterImpl(new BufferedSystemStreams().getSystemOut(), PrinterVerboseLevel.NONE);

        cmd = new CheckCommandImpl (daoFactoryLoader, xmlReader, new SchemaDifferencePrinterImpl(), StreamFactory.getInstance(), SystemStreams.getInstance());
    }

    @Test
    public void equalSchemas_execute_isSchemasEqualReturnsTrue() throws Exception {
        dbSchema = DbSchemas.createDbSchema("db");
        xmlSchema = DbSchemas.createDbSchema("xml");
        cmd.execute(jdbcParams, null, null, null, PrinterVerboseLevel.NONE);
        assertTrue(cmd.isSchemasEqual());
    }

    @Test
    public void notEqualSchemas_execute_isSchemasEqualReturnsFalse() throws Exception {
        dbSchema = DbSchemas.createDbSchema("db");
        dbSchema.getTables().add(Tables.createTableWithColumns());
        xmlSchema = DbSchemas.createDbSchema("xml");

        cmd.execute(jdbcParams, null, null, null, PrinterVerboseLevel.NONE);
        assertFalse(cmd.isSchemasEqual());
        assertEquals(1, closeDAOCallCount);
        assertEquals(1, initDAOCallCount);
    }


    @Test
    public void badJdbcConnectionInfo_execute_throwsExecuteCommandException() throws Exception {
        throwExceptionOnDaoOperation = true;

        Exception e=null;

        try { cmd.execute(jdbcParams, null, null, null, PrinterVerboseLevel.NONE); }
        catch (ExecuteCommandException ex) { e = ex;}

        assertTrue(e.getCause().getMessage().contains(DAOError));
        assertEquals(1, closeDAOCallCount);
        assertEquals(1, initDAOCallCount);
    }

    @Test
    public void badInputFileName_parse_throwsExecuteCommandException() throws Exception {
        Exception e=null;

        try { cmd.execute(jdbcParams, null,  "tt:\\badFileName.txt", null, PrinterVerboseLevel.NONE); }
        catch (ExecuteCommandException ex) { e = ex;}

        assertTrue(e.getCause() instanceof FileNotFoundException);
    }

    @Test
    public void badIgnoreListFileName_parse_throwsExecuteCommandException() throws Exception {
        Exception e=null;

        try { cmd.execute(jdbcParams, null,  null, "tt:\\badFileName.txt",  PrinterVerboseLevel.NONE); }
        catch (ExecuteCommandException ex) { e = ex;}

        assertTrue(e.getCause() instanceof FileNotFoundException);
    }
}