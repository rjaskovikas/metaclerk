package org.rola.metaclerk.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseJdbcConnectorImplTest extends BaseJdbcConnectorImpl {

	private int loadDriverCallCount;
	private int getJdbcConnectionCount;
	private String conStr;
	private String usrName;
	private String pswd;

	private Connection con;
	private Statement stat;
	private ResultSet res;
	
	private final String CONNECTION_STR = "Same connn";
	private final String USER_NAME = "user";
	private final String PASSWORD = "password";


	@Before
	public void setUp() {
		resetDriverLoadFlag();
	}
	
	private void resetDriverLoadFlag() {
		driverLoaded = false;
	}	
	
	@Test
	public void connectionInfo_connect_connectionInfoPassedCorrectly() throws Exception {
		connect(CONNECTION_STR, USER_NAME, PASSWORD);
		
		assertEquals(CONNECTION_STR, conStr);
		assertEquals(USER_NAME, usrName);
		assertEquals(PASSWORD, pswd);
	}
	
	@Test
	public void firstConnectMethodUse_connect_loadsJdbcDriver () throws SQLException {
		connect(CONNECTION_STR, USER_NAME, PASSWORD);
		
		assertEquals(1, loadDriverCallCount);
		assertEquals(1, getJdbcConnectionCount);
	}
	
	@Test
	public void secondConnectMechodUse_connect_doesntLoadJdbcDriver() throws Exception {
		connect(CONNECTION_STR, USER_NAME, PASSWORD);
		connect(CONNECTION_STR, USER_NAME, PASSWORD);
		
		assertEquals(1, loadDriverCallCount);
		assertEquals(2, getJdbcConnectionCount);
	}
	
	@Test
	public void openConnection_close_cleansConnectionStatemet() throws Exception {
		connect(CONNECTION_STR, USER_NAME, PASSWORD);
		assertNotNull(sqlCon);
		
		close();
		assertNull(sqlCon);
	}
	
	@Test
	public void openStatemet_close_cleansStatemetAndResultSetObjects() throws Exception {
		mockConnectionAndStatement();
		
		close();
		
		assertNull(sqlCon);
		assertNull(sqlStatement);
		InOrder order = inOrder(con, stat); 
		order.verify(stat, times(1)).close();
		order.verify(con).close();
		order.verifyNoMoreInteractions();
	}

	@Test
	public void openStatementWithResult_close_cleansConnectionStatementAndResultSetObjects() throws Exception {
		mockConnectionStatemenAndResultSet();
		
		close();
		
		assertNull(sqlCon);
		assertNull(sqlStatement);
		assertNull(sqlResultSet);
		InOrder order = inOrder(con, stat, res);
		order.verify(res).close();
		order.verify(stat).close();
		order.verify(con).close();
		order.verifyNoMoreInteractions();
	}

//	@Test
//	public void badDriverName_connect_throwsSqlException() throws Exception {
//		callLaodDriver = true;
//		this.db_driver_string = "nonsense";
//		resetDriverLoadFlag();
//
//		try {
//			connect(null, null, null);
//			assertFalse(true);
//		} catch (SQLRuntimeException ex) {
//			assertTrue(ex.getCause().toString().contains("driver"));
//			return;
//		}
//		assertFalse(true);
//	}
//
//	@Test
//	public void correctDriverName_connect_doesntThrowsSqlException() throws Exception {
//		callLaodDriver = true;
//		resetDriverLoadFlag();
//
//		try {
//			connect(null, null, null);
//		} catch (SQLRuntimeException ex) {
//			assertTrue(false);
//		}
//	}

	private void mockConnectionAndStatement() {
		sqlCon = mock(Connection.class);
		con = sqlCon;
		sqlStatement = mock(PreparedStatement.class);
		stat = sqlStatement;
	}

	private void mockConnectionStatemenAndResultSet() {
		mockConnectionAndStatement();
		sqlResultSet = mock(ResultSet.class);
		res = sqlResultSet;
	}
	
	@Override
	public void createNewJdbcConnection(String connectionString, String userName, String password) throws SQLException {
		this.conStr = connectionString;
		this.usrName = userName;
		this.pswd = password;
		getJdbcConnectionCount++;
		sqlCon = mock(Connection.class);
	}

	@Override
	public void safeLoadDriver() throws ClassNotFoundException {
		loadDriverCallCount++;
	}
	
}
