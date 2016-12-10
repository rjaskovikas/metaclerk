package org.rola.metaclerk.plugin.oracle.dao.impl;


import org.rola.metaclerk.dao.api.*;
import org.rola.metaclerk.exception.dao.SQLRuntimeException;
import org.rola.metaclerk.model.*;

import java.sql.SQLException;

public class OracleSchemaDAOImpl implements SchemaTableListDAO, TableColumnsDAO, AllSchemaObjectsDAO, DbPrivilegesDAO {

	JdbcConnector dbc = null;

	public OracleSchemaDAOImpl(JdbcConnector dbc) {
		this.dbc = dbc;
	}

	@Override
	public void initDAO(String jdbcConnectionStr, String dbUser, String dbPassword) {
		dbc.connect(jdbcConnectionStr, dbUser, dbPassword);
	}

	@Override
	public void closeDAO() {
		try {
			dbc.close();
		} catch (SQLException ex) {
			throw new SQLRuntimeException("Error closing jdbc connection", ex);
		}
	}

	public TableList getSchemaTableList(String schema) {
		OracleSchemaTableListFetcher f = new OracleSchemaTableListFetcher(dbc, "TABLE");
		return f.fetchTableList(schema);
	}

	ViewList getSchemaViewList(String schema) {
		OracleSchemaTableListFetcher f = new OracleSchemaTableListFetcher(dbc, "VIEW");
		return ViewList.formTableList(f.fetchTableList(schema));
	}

	public void fillTableColumnDescription(TableDescription tableDescription) {
		OracleTableColumnsFetcher f = new OracleTableColumnsFetcher(dbc);
		f.fillTableColumns(tableDescription);
	}

	void fillViewColumnDescription(ViewDescription viewDescription) {
		fillTableColumnDescription(viewDescription);
	}

	@Override
	public DbSchema getAllSchemaObjects(String schemaName) {
		DbSchema schema = new DbSchema();
		schema.setName(schemaName);
		schema.setTables(getAllTables(schemaName));
		schema.setViews(getAllViews(schemaName));
		schema.setPrivileges(getDbUserPrivilegeList(schemaName));
		return schema;
	}

	@Override
	public PrivilegeList getDbUserPrivilegeList(String dbUserName) {
		OracleDbPrivilegesFetcher f = new OracleDbPrivilegesFetcher(dbc);
		return f.fetchPrivilegeList(dbUserName);
	}


	private ViewList getAllViews(String schemaName) {
		ViewList lst = getSchemaViewList(schemaName);
		if (lst == null) return null;
		lst.forEach(this::fillViewColumnDescription);
		return lst;
	}

	private TableList getAllTables(String schemaName) {
		TableList lst = getSchemaTableList(schemaName);
		lst.forEach(this::fillTableColumnDescription);
		return lst;
	}
}
