package org.rola.metaclerk.plugin.oracle.dao.impl;

import java.sql.SQLException;

import org.rola.metaclerk.dao.api.JdbcConnector;
import org.rola.metaclerk.dao.api.JdbcResult;
import org.rola.metaclerk.dao.impl.BaseFetcher;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.TableList;

class OracleSchemaTableListFetcher extends BaseFetcher {
	private final String objectType;
	
	public OracleSchemaTableListFetcher(JdbcConnector con, String objectType) {
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
		dbc.executeSelectStatement("select * from all_objects where lower(owner) = ? and upper(object_type) = ?",
				schema.toLowerCase(), objectType.toUpperCase());
		dbc.forEachDbRow(result->lst.add(fetchTableDescriptionFromDd(result)));
		return lst;
	}
	
	private TableDescription fetchTableDescriptionFromDd(JdbcResult jdbcResult) throws SQLException {
		TableDescription td = new TableDescription();
		td.setName(jdbcResult.getStringResult("OBJECT_NAME"));
		td.setOwnerName(jdbcResult.getStringResult("OWNER"));
		return td;
	}
}
