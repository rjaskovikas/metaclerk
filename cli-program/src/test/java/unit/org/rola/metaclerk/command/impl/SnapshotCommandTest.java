package org.rola.metaclerk.command.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.exception.ExecuteCommandException;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.plugin.api.DaoFactory;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;
import org.rola.metaclerk.test.BufferedSystemStreams;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.xml.api.SchemaXmlWriter;

import java.io.FileNotFoundException;

public class SnapshotCommandTest extends SnapshotCommandImpl implements DynamicDAOFactoryLoader, DaoFactory {

	private final SchemaXmlWriter xmlWriterMock;
	private JdbcConnectionParam param;
	private DbSchema sc;
	private AllSchemaObjectsDAO daoMock = mock(AllSchemaObjectsDAO.class);

	@Before
	public void setUp() throws Exception {
		param = new JdbcConnectionParam("con", "user", "pwd", "dbType");
	}

	public SnapshotCommandTest() {
		super( null, mock(SchemaXmlWriter.class),
				mock(StreamFactory.class), new BufferedSystemStreams());
		this.daoFactoryLoader = this;
		xmlWriterMock = this.schemaXmlWriter;
	}

	@Test
	public void testExecutionOrder() throws Exception {
		when(daoMock.getAllSchemaObjects("d")).thenReturn(createDbSchema());
		execute(param, "d", null);
		
		InOrder order = inOrder(xmlWriterMock, daoMock);
		order.verify(daoMock).initDAO(anyString(), anyString(), anyString());
		order.verify(daoMock).getAllSchemaObjects("d");
		order.verify(daoMock).closeDAO();
		order.verify(xmlWriterMock).serializeDbSchema(sc, this.systemStreams.getSystemOut());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void badJdbcConnection_execute_throwsExecuteCommandException() throws Exception {
		doThrow(new DAOException("Error using JDBC connection", null)).when(daoMock).getAllSchemaObjects("d");
		Exception e = null;

		try {
			execute(param, "d", null);
		} catch (ExecuteCommandException ex) { e = ex;}

		assertTrue(e.getCause().getMessage().equals("Error using JDBC connection"));
		verify(daoMock, times(1)).initDAO(anyString(), anyString(), anyString());
		verify(daoMock, times(1)).getAllSchemaObjects(anyString());
		verify(daoMock, times(1)).closeDAO();
	}

	@Test
	public void fileParamWithBadNameToThrowFileNotFoundException_parse_printsErrorMessage() throws Exception {
		doThrow(new FileNotFoundException("file")).when(streamFactory).createPrintStream(anyString());
		Exception e = null;
		try {
			execute(param, "d", "rr:\\uu.txt");
		} catch (ExecuteCommandException ex) { e = ex;}

		assertTrue(e.getCause().getMessage().equals("file"));
	}


	private DbSchema createDbSchema() {
		sc = new DbSchema();
		return sc;
	}

	@Override
	public AllSchemaObjectsDAO createAllSchemaObjectsDAO() {
		return daoMock;
	}

	@Override
	public DaoFactory loadDaoPluginFactory(String dbType) {
		assertEquals("dbType", dbType);
		return this;
	}
}
