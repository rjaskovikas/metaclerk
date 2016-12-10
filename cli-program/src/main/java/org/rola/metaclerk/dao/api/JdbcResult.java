package org.rola.metaclerk.dao.api;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface JdbcResult {
	String getStringResult(String colName) throws SQLException;
	Integer getIntResult(String colName) throws SQLException;
	Double getDoubleResult(String colName) throws SQLException;
	BigDecimal getBigDecimalResult(String colName) throws SQLException;

	String getStringResult(int columnIndex) throws SQLException;
	Integer getIntResult(int columnIndex) throws SQLException;
	Double getDoubleResult(int columnIndex) throws SQLException;
	BigDecimal getBigDecimalResult(int columnIndex) throws SQLException;
}