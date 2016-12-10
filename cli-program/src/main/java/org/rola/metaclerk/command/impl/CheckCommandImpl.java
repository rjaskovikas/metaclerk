package org.rola.metaclerk.command.impl;


import org.rola.metaclerk.command.api.CheckCommand;
import org.rola.metaclerk.comparator.impl.SchemaComparatorImpl;
import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.plugin.api.DaoFactory;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;
import org.rola.metaclerk.exception.ExecuteCommandException;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.printers.api.SchemaDifferencePrinter;
import org.rola.metaclerk.printers.impl.MessagePrinterImpl;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.SystemStreams;
import org.rola.metaclerk.xml.api.SchemaXmlReader;
import org.rola.metaclerk.xml.impl.IgnoreTablesXmlReaderImpl;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class CheckCommandImpl implements CheckCommand {
    private final SchemaXmlReader schemaXmlReader;
    private AllSchemaObjectsDAO dao;
    private final DynamicDAOFactoryLoader daoFactoryLoader;
    private final MessagePrinter messagePrinter;

    private DbSchema expectedSchema;
    private DbSchema actualSchema;

    private String schemaName;
    private JdbcConnectionParam jdbcParams;
    private List<String> ignoreTableList;
    private final SchemaComparatorImpl schemaComp = createDbSchemaComparator();
    private final SchemaDifferencePrinter schemaDifferencePrinter;
    private String ignoreListFileName;
    private String inputFileName;
    private StreamFactory streamFactory;
    private SystemStreams systemStreams;

    public CheckCommandImpl(DynamicDAOFactoryLoader daoFactoryLoader, SchemaXmlReader schemaXmlReader,
                            SchemaDifferencePrinter schemaDifferencePrinter, StreamFactory streamFactory,
                            SystemStreams systemStreams) {
        this.daoFactoryLoader = daoFactoryLoader;
        this.messagePrinter = new MessagePrinterImpl(systemStreams.getSystemOut(), PrinterVerboseLevel.NONE);
        this.schemaXmlReader = schemaXmlReader;
        this.schemaDifferencePrinter = schemaDifferencePrinter;
        this.streamFactory = streamFactory;
        this.systemStreams = systemStreams;
    }

    @Override
    public void execute(JdbcConnectionParam jdbcParams, String schemaName, String inputFileName,
                        String ignoreListFileName, PrinterVerboseLevel verboseLevel) {
        init(jdbcParams, schemaName, inputFileName, ignoreListFileName, verboseLevel);
        try {
            executeSafe();
        } catch (DAOException | XMLStreamException | FileNotFoundException ex) {
            throw new ExecuteCommandException("Error executing check command", ex);
        }
    }

    private void executeSafe() throws XMLStreamException, FileNotFoundException {
        createDaoObject();
        loadSchemasAndIgnoreLists();
        compareSchemas();
        printComparisonResult();
    }

    private void createDaoObject() {
        DaoFactory factory = daoFactoryLoader.loadDaoPluginFactory(jdbcParams.getDbType());
        dao = factory.createAllSchemaObjectsDAO();
    }

    private InputStream getIgnoreListStream() throws FileNotFoundException {
        return streamFactory.createInputStream(ignoreListFileName);
    }

    private InputStream getInputStream() throws FileNotFoundException {
        if (inputFileName == null)
            return systemStreams.getSystemIn();
        return streamFactory.createInputStream(inputFileName);
    }

    private void printVerdict() {
        if (isSchemasEqual())
            messagePrinter.printlnV("Schemas are equal :) ...");
        else
            messagePrinter.printlnV("!!! Schemas are different !!!");
    }

    private void printComparisonResult() {
        schemaDifferencePrinter.print(schemaComp.getSchemaDifference(), messagePrinter);
        printVerdict();
    }

    private void loadSchemasAndIgnoreLists() throws XMLStreamException, FileNotFoundException {
        loadIgnoreList();
        expectedSchema = loadSchemaFromXml();
        actualSchema = loadSchemaFromDb();
    }

    private void loadIgnoreList() throws XMLStreamException, FileNotFoundException {
        if (ignoreListFileName != null) {
                ignoreTableList = new IgnoreTablesXmlReaderImpl().deserializeTableList(getIgnoreListStream());
        }
    }

    private void compareSchemas() {
        if (ignoreTableList != null)
            schemaComp.setIgnoreTableList(ignoreTableList);
        schemaComp.compareEqual(expectedSchema, actualSchema);
    }

    private SchemaComparatorImpl createDbSchemaComparator() {
        return new SchemaComparatorImpl();
    }

    private DbSchema loadSchemaFromXml() throws XMLStreamException, FileNotFoundException {
            return schemaXmlReader.deserializeSchema(getInputStream());
    }

    private DbSchema loadSchemaFromDb() {
        dao.initDAO(jdbcParams.getJdbcConnectionStr(), jdbcParams.getUserName(), jdbcParams.getPassword());
        try {
            return dao.getAllSchemaObjects(schemaName);
        } finally {
            dao.closeDAO();
        }
    }

    private void init(JdbcConnectionParam jdbcParams, String schemaName, String inputFileName, String ignoreListFileName,
                      PrinterVerboseLevel verboseLevel) {
        this.jdbcParams = jdbcParams;
        this.schemaName = schemaName;
        this.inputFileName = inputFileName;
        this.ignoreListFileName = ignoreListFileName;
        messagePrinter.setVerboseLevel(verboseLevel);
    }

    @Override
    public boolean isSchemasEqual() {
        return schemaComp.isSchemasEqual();
    }

}
