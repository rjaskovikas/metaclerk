package org.rola.metaclerk.plugin.postgresql.dao.impl;

import org.rola.metaclerk.dao.api.JdbcConnector;
import org.rola.metaclerk.dao.api.JdbcResult;
import org.rola.metaclerk.dao.impl.BaseFetcher;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.model.PrivilegeList;

import java.sql.SQLException;

class PGSqlDbPrivilegesFetcher extends BaseFetcher {

    public PGSqlDbPrivilegesFetcher(JdbcConnector con) {
        setConnector(con);
    }

    public PrivilegeList fetchPrivilegeList(String dbUserName) {
        try {
            return getSafePrivilegeList(dbUserName);
        } catch (Exception ex) {
            throw new DAOException("Error fetching schema privileges", ex);
        }
    }

    private PrivilegeList getSafePrivilegeList(String dbUserName) throws SQLException {
        PrivilegeList lst = new PrivilegeList();
        String query = "select * from table_privileges where grantee = ?";
        dbc.executeSelectStatement(query, dbUserName);
        dbc.forEachDbRow(dc -> lst.add(fetchTableDescriptionFromDd(dc)));
        return lst;
    }

    private DbPrivilege fetchTableDescriptionFromDd(JdbcResult result) throws SQLException {
        DbPrivilege priv = new DbPrivilege();
        priv.setOwnerName(result.getStringResult("TABLE_SCHEMA"));
        priv.setName(result.getStringResult("TABLE_NAME"));
        priv.setPrivilege(result.getStringResult("PRIVILEGE_TYPE"));
        priv.setGrantable(result.getStringResult("IS_GRANTABLE"));
        return priv;
    }
}
