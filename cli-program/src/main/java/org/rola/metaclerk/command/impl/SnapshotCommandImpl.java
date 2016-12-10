package org.rola.metaclerk.command.impl;

import org.rola.metaclerk.command.api.SnapshotCommand;
import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.exception.ExecuteCommandException;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.plugin.api.DaoFactory;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.SystemStreams;
import org.rola.metaclerk.xml.api.SchemaXmlWriter;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class SnapshotCommandImpl implements SnapshotCommand {
	AllSchemaObjectsDAO dao;
	DynamicDAOFactoryLoader daoFactoryLoader;
	private DbSchema schema;

	private String schemaName;
	private JdbcConnectionParam jdbcParams;
	protected final SchemaXmlWriter schemaXmlWriter;
	private String outputFileName;
	StreamFactory streamFactory;
	SystemStreams systemStreams;

	public SnapshotCommandImpl(DynamicDAOFactoryLoader daoFactoryLoader, SchemaXmlWriter schemaXmlWriter, StreamFactory streamFactory,
							   SystemStreams systemStreams) {
		this.daoFactoryLoader = daoFactoryLoader;
		this.schemaXmlWriter = schemaXmlWriter;
		this.streamFactory = streamFactory;
		this.systemStreams = systemStreams;
	}

	@Override
	public void execute(JdbcConnectionParam jdbcParams, String schemaName, String outputFileName) {
		init(jdbcParams, schemaName, outputFileName);
		try {
			executeSafe();
		} catch (DAOException | FileNotFoundException ex) {
			throw new ExecuteCommandException("Error executing snapshot command:", ex);
		}
	}

	private void init(JdbcConnectionParam jdbcParams, String schemaName, String outputFileName) {
		this.schemaName = schemaName;
		this.outputFileName = outputFileName;
		this.jdbcParams = jdbcParams;
	}

	private void executeSafe() throws FileNotFoundException {
		createDaoObject();
		readDbSchemaObjectsFromDb();
		serializeSchemaToStream();
	}

	private void createDaoObject() {
		DaoFactory factory = daoFactoryLoader.loadDaoPluginFactory(jdbcParams.getDbType());
		dao = factory.createAllSchemaObjectsDAO();
	}

	private void readDbSchemaObjectsFromDb() {
		dao.initDAO(jdbcParams.getJdbcConnectionStr(), jdbcParams.getUserName(), jdbcParams.getPassword());
		try {
			schema = dao.getAllSchemaObjects(schemaName);
		} finally {
			dao.closeDAO();
		}
	}

	private void serializeSchemaToStream() throws FileNotFoundException {
		schemaXmlWriter.serializeDbSchema(schema, getOutputStream());
	}

	private PrintStream getOutputStream() throws FileNotFoundException {
		if (outputFileName == null)
			return systemStreams.getSystemOut();
		return streamFactory.createPrintStream(outputFileName);
	}
}
