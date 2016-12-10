package org.rola.metaclerk.dao.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BaseJdbcConnector1Test extends BaseJdbcConnectorImpl {

	private String sqlStatement;
	private int sqlExecuted;
	private final List<Object> paramValues = new ArrayList<>();
	private final List<Integer> paramIdxs = new ArrayList<>();
	private final List<String> paramType = new ArrayList<>();
	
	
	@Test
	public void noParameters_Execute_NoSetStringParamCalls() throws SQLException {
		String sqlQuery = "no matter";
		executeSelectStatement(sqlQuery);

		assertEquals(sqlQuery, this.sqlStatement);
		assertEquals(0, paramIdxs.size());
		assertEquals(1, sqlExecuted);
	}
	
	@Test
	public void oneStringParameter_Execute_OneSetStringParamCall() throws SQLException {
		String sqlQuery = "no matter";
		executeSelectStatement(sqlQuery, "first param");

		assertEquals(sqlQuery, this.sqlStatement);
		assertEquals(1, paramIdxs.size());
		checkParam("first param", 1, "String");
		assertEquals(1, sqlExecuted);
	}

	@Test
	public void oneIntegerParameter_Execute_callOnceSetIntParamCall() throws SQLException {
		String sqlQuery = "no matter";
		executeSelectStatement(sqlQuery, (Integer) 10);

		assertEquals(sqlQuery, this.sqlStatement);
		assertEquals(1, paramIdxs.size());
		checkParam(10, 1, "Integer");
		assertEquals(1, sqlExecuted);
	}

	@Test
	public void oneFloatParameter_Execute_callOnceSetFloatParamCall() throws SQLException {
		String sqlQuery = "no matter";
		executeSelectStatement(sqlQuery, 10.5f);

		assertEquals(sqlQuery, this.sqlStatement);
		assertEquals(1, paramIdxs.size());
		checkParam(10.5, 1, "Double");
		assertEquals(1, sqlExecuted);
	}

	@Test
	public void oneDigDecimalParameter_Execute_callOnceSetBigDecimalParamCall() throws SQLException {
		String sqlQuery = "no matter";
		executeSelectStatement(sqlQuery, new BigDecimal("10.3"));

		assertEquals(sqlQuery, this.sqlStatement);
		assertEquals(1, paramIdxs.size());
		checkParam(new BigDecimal("10.3"), 1, "BigDecimal");
		assertEquals(1, sqlExecuted);
	}

	@Test
	public void oneShortParameter_Execute_callOnceSetIntParamCall() throws SQLException {
		String sqlQuery = "no matter";
		executeSelectStatement(sqlQuery, Short.parseShort("3"));

		assertEquals(sqlQuery, this.sqlStatement);
		assertEquals(1, paramIdxs.size());
		checkParam(3, 1, "Integer");
		assertEquals(1, sqlExecuted);
	}

	@Test
	public void oneByteParameter_Execute_callOnceSetIntParamCall() throws SQLException {
		String sqlQuery = "no matter";
		executeSelectStatement(sqlQuery, Byte.parseByte("4"));

		assertEquals(sqlQuery, this.sqlStatement);
		assertEquals(1, paramIdxs.size());
		checkParam(4, 1, "Integer");
		assertEquals(1, sqlExecuted);
	}

	@Test
	public void twoStringParameter_Execute_twoSetStringParamCall() throws SQLException {
		String sqlQuery = "no matter";
		executeSelectStatement(sqlQuery, "first param", "second param");
		
		assertEquals(sqlQuery, this.sqlStatement);
		assertEquals(2, paramIdxs.size());
		checkParam("first param", 1, "String");
		checkParam("second param", 2, "String");
		assertEquals(1, sqlExecuted);
	}
	
	public void setStringParam(int index, String param) throws SQLException {
		paramValues.add(param);
		paramIdxs.add(index);
		paramType.add("String");
	}

	@Override
	protected void setDoubleNumber(int index, Double param) throws SQLException {
		paramValues.add(param);
		paramIdxs.add(index);
		paramType.add("Double");
	}

	@Override
	protected void setBigDecimalParam(int index, BigDecimal param) throws SQLException {
		paramValues.add(param);
		paramIdxs.add(index);
		paramType.add("BigDecimal");
	}

	@Override
	protected void setIntParam(int index, Integer param) throws SQLException {
		paramValues.add(param);
		paramIdxs.add(index);
		paramType.add("Integer");
	}

	public void prepareStatement(String sqlStatement) throws SQLException {
		this.sqlStatement = sqlStatement;
	} 
	
	public void executeQuery() throws SQLException {
		sqlExecuted++;
	}

	private void checkParam(Object value, int index, String type) {
		assertEquals(value, paramValues.get(index-1));
		assertEquals((Integer)index, paramIdxs.get(index-1));
		assertEquals(type, this.paramType.get(index-1));
	}

	@Override
	protected void safeLoadDriver() throws ClassNotFoundException {

	}
}
