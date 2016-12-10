package org.rola.metaclerk.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.rola.metaclerk.dao.api.JdbcConnector;
import org.rola.metaclerk.dao.api.JdbcResult;
import org.rola.metaclerk.exception.dao.SQLRuntimeException;


public abstract class BaseJdbcConnectorImpl implements JdbcConnector, JdbcResult {
	Connection sqlCon = null;
	PreparedStatement sqlStatement = null;
	ResultSet sqlResultSet = null;
	static boolean driverLoaded = false;
	
	public void connect(String connectionString, String userName, String password) {
		try {
			loadDriver();
			cleanUpCurrentConnectionObjects();
			createNewJdbcConnection(connectionString, userName, password);
		} catch (SQLException ex) {
			throw new SQLRuntimeException("Can't connect to database:", ex);
		}
	}

	void createNewJdbcConnection(String connectionString, String userName, String password) throws SQLException {
		sqlCon = DriverManager.getConnection(
			        connectionString, userName, password);
	}

	private void loadDriver() throws SQLException {
		if (!driverLoaded) {
			try {
				safeLoadDriver();
			} catch (ClassNotFoundException e) {
				throw new SQLException("Can't load db driver", e);
			}
			driverLoaded = true;
		}
	}

	protected abstract void safeLoadDriver() throws ClassNotFoundException;

	@Override
	public int executeUpdate(String sqlStatement, Object... params) throws SQLException {
		prepareStatement(sqlStatement);
		setStatementParams(params);
		return this.sqlStatement.executeUpdate();
	}

	private void setStatementParams(Object... params) throws SQLException {
		int idx = 1;
		for (Object param : params) {
			if (isInteger(param))
				setIntParam(idx, ((Number)param).intValue());
			else if (isRealNumber(param))
				setDoubleNumber(idx, ((Number) param).doubleValue());
			else if (isBigDecimal(param))
				setBigDecimalParam(idx, (BigDecimal) param);
			else
				setStringParam(idx, param.toString());
			idx++;
		}
	}

	void setDoubleNumber(int index, Double param) throws SQLException {
		sqlStatement.setDouble(index, param);
	}

	private boolean isBigDecimal(Object pr) {
		return pr instanceof BigDecimal;
	}

	void setBigDecimalParam(int index, BigDecimal param) throws SQLException {
		sqlStatement.setBigDecimal(index, param);
	}

	private boolean isRealNumber(Object pr) {
		return pr instanceof Double || pr instanceof Float;
	}

	private boolean isInteger(Object pr) {
		return pr instanceof Integer || pr instanceof Short || pr instanceof Byte;
	}


	public void prepareStatement(String sqlStatement) throws SQLException {
		cleanUpResultAndStatement();
		this.sqlStatement = sqlCon.prepareStatement(sqlStatement);
	}

	public void setStringParam(int index, String param) throws SQLException {
		sqlStatement.setString(index, param);
	}

	void setIntParam(int idx, Integer pr) throws SQLException {
		sqlStatement.setInt(idx, pr);
	}

	public boolean nextRow() throws SQLException {
		return sqlResultSet.next();
	}

	public String getStringResult(String colName) throws SQLException {
		return sqlResultSet.getString(colName);
	}

	public Integer getIntResult(String colName) throws SQLException {
		int result = sqlResultSet.getInt(colName);
		return sqlResultSet.wasNull() ? null : result;
	}

	public Double getDoubleResult(String colName) throws SQLException {
		return sqlResultSet.getDouble(colName);
	}

	public BigDecimal getBigDecimalResult(String colName) throws SQLException {
		return sqlResultSet.getBigDecimal(colName);
	}

	public String getStringResult(int columnIndex) throws SQLException {
		return sqlResultSet.getString(columnIndex);
	}

	public Integer getIntResult(int columnIndex) throws SQLException {
		int result = sqlResultSet.getInt(columnIndex);
		return sqlResultSet.wasNull() ? null : result;
	}

	public Double getDoubleResult(int columnIndex) throws SQLException {
		return sqlResultSet.getDouble(columnIndex);
	}

	public BigDecimal getBigDecimalResult(int columnIndex) throws SQLException {
		return sqlResultSet.getBigDecimal(columnIndex);
	}

	private void cleanUpCurrentConnectionObjects() throws SQLException {
		cleanUpResultAndStatement();
		cleanUpConnection();
	}
	
	private void cleanUpConnection() throws SQLException {
		if (sqlCon != null) {
			safeCloseConnection();
			sqlCon = null;
		}
	}

	public void cleanUpResultAndStatement() throws SQLException {
		cleanUpResultSet();
		cleanUpStatement();
	}

	private void cleanUpStatement() throws SQLException {
		if (sqlStatement != null) {
			safeCloseStatement();
			sqlStatement = null;
		}
	}

	private void safeCloseStatement() throws SQLException {
		sqlStatement.close();
	}

	private void cleanUpResultSet() throws SQLException {
		if (sqlResultSet != null) {
			safeCloseResultSet();
			sqlResultSet = null;
		}	
	}

	private void safeCloseResultSet() throws SQLException {
		sqlResultSet.close();
	}

	public void executeQuery() throws SQLException {
		sqlResultSet = sqlStatement.executeQuery();	
	}

	public void close() throws SQLException {
		cleanUpCurrentConnectionObjects();
	}

	private void safeCloseConnection() throws SQLException {
		sqlCon.close();
	}
	
	public void executeSelectStatement(String sqlQuery, Object... params) throws SQLException {
		prepareStatement(sqlQuery);
		setStatementParams(params);
		executeQuery();
	}

	public void forEachDbRow(DbRowFetcher reader) throws SQLException {
		while(nextRow())
			reader.fetchRowFromDb(this);
	}


}
