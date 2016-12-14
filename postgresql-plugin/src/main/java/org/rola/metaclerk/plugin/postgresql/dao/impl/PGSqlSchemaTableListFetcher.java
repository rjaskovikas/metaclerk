package org.rola.metaclerk.plugin.postgresql.dao.impl;

import org.rola.metaclerk.dao.api.JdbcConnector;
import org.rola.metaclerk.dao.api.JdbcResult;
import org.rola.metaclerk.dao.impl.BaseFetcher;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.TableList;

import java.sql.SQLException;

class PGSqlSchemaTableListFetcher extends BaseFetcher {
	private final String objectType;
	
	public PGSqlSchemaTableListFetcher(JdbcConnector con, String objectType) {
		setConnector(con);
		this.objectType = objectType;
	}

	public TableList fetchTableList(String schema) {
		try {
			return getSafeTableList(schema);
		} catch (Exception ex) {
			throw new DAOException("Error fetching database schema objects:", ex);
		}
	}

	private TableList getSafeTableList(String schema) throws SQLException {
		TableList lst = new TableList();
		String query = "select * from tables where table_schema = ? and table_type = ?";
		dbc.executeSelectStatement(query, schema, objectType);
		dbc.forEachDbRow(result->lst.add(fetchTableDescriptionFromDd(result)));
		return lst;
	}
	
	private TableDescription fetchTableDescriptionFromDd(JdbcResult jdbcResult) throws SQLException {
		TableDescription td = new TableDescription();
		td.setName(jdbcResult.getStringResult("TABLE_NAME"));
		td.setOwnerName(jdbcResult.getStringResult("TABLE_SCHEMA"));
		return td;
	}
}
