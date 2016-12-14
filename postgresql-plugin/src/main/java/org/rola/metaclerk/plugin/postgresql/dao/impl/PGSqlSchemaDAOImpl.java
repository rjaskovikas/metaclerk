package org.rola.metaclerk.plugin.postgresql.dao.impl;

import org.rola.metaclerk.dao.api.*;
import org.rola.metaclerk.exception.dao.SQLRuntimeException;
import org.rola.metaclerk.model.*;

import java.sql.SQLException;

public class PGSqlSchemaDAOImpl implements SchemaTableListDAO, TableColumnsDAO, AllSchemaObjectsDAO, DbPrivilegesDAO {

    JdbcConnector dbc = null;

    public PGSqlSchemaDAOImpl(JdbcConnector dbc) {
        this.dbc = dbc;
    }

    void setDbc(JdbcConnector dbc) {
        this.dbc = dbc;
    }

    @Override
    public void initDAO(String jdbcConnectionStr, String dbUser, String dbPassword) {
        dbc.connect(jdbcConnectionStr, dbUser, dbPassword);
        selectInformationSchema();
    }

    @Override
    public void closeDAO() {
        try {
            dbc.close();
        } catch (SQLException ex) {
            throw new SQLRuntimeException("Error on closing jdbc connection", ex);
        }
    }

    void selectInformationSchema() {
        try {
            dbc.executeUpdate("set schema 'information_schema'");
        } catch (SQLException ex) {
            throw new SQLRuntimeException("Can't use information_schema in postgresql", ex);
        }
    }

    public TableList getSchemaTableList(String schema) {
        PGSqlSchemaTableListFetcher f = new PGSqlSchemaTableListFetcher(dbc, "BASE TABLE");
        return f.fetchTableList(schema);
    }

    ViewList getSchemaViewList(String schema) {
        PGSqlSchemaTableListFetcher f = new PGSqlSchemaTableListFetcher(dbc, "VIEW");
        return ViewList.formTableList(f.fetchTableList(schema));
    }

    public void fillTableColumnDescription(TableDescription tableDescription) {
        PGSqlTableColumnsFetcher f = new PGSqlTableColumnsFetcher(dbc);
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
        PGSqlDbPrivilegesFetcher f = new PGSqlDbPrivilegesFetcher(dbc);
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
