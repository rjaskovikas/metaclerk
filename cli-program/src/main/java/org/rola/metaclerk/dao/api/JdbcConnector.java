package org.rola.metaclerk.dao.api;

import java.sql.SQLException;

public interface JdbcConnector {
	@SuppressWarnings("UnusedReturnValue")
	int executeUpdate(String sqlQuery, Object... params) throws SQLException;

	 interface DbRowFetcher {
		void fetchRowFromDb(JdbcResult con) throws SQLException;
	}
	
	void connect(String connectionString, String userName, String password);
	void prepareStatement(String sqlStatement) throws SQLException;
	void executeQuery() throws SQLException;
	void setStringParam(int index, String param) throws SQLException;
	boolean nextRow() throws SQLException;
	void cleanUpResultAndStatement() throws SQLException;
	void close() throws SQLException;
	
	void executeSelectStatement(String sqlQuery, Object... params) throws SQLException;
	void forEachDbRow(DbRowFetcher reader) throws SQLException;
}
