package org.rola.metaclerk.plugin.oracle.dao.impl;

import org.rola.metaclerk.dao.api.JdbcConnector;
import org.rola.metaclerk.dao.api.JdbcResult;
import org.rola.metaclerk.dao.impl.BaseFetcher;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.model.PrivilegeList;

import java.sql.SQLException;

class OracleDbPrivilegesFetcher extends BaseFetcher {

    public OracleDbPrivilegesFetcher(JdbcConnector con) {
        setConnector(con);
    }

    public PrivilegeList fetchPrivilegeList(String schema) {
        try {
            return getSafePrivilegeList(schema);
        } catch (Exception ex) {
            throw new DAOException("Error fetching schema privileges", ex);
        }
    }

    private PrivilegeList getSafePrivilegeList(String schemaName) throws SQLException {
        PrivilegeList lst = new PrivilegeList();
        dbc.executeSelectStatement("select grantor, table_name, privilege, grantable" +
                        " from user_tab_privs where lower(grantee) = ? ", schemaName.toLowerCase());
        dbc.forEachDbRow(dc -> lst.add(fetchTableDescriptionFromDd(dc)));
        return lst;
    }

    private DbPrivilege fetchTableDescriptionFromDd(JdbcResult result) throws SQLException {
        DbPrivilege priv = new DbPrivilege();
        priv.setOwnerName(result.getStringResult("GRANTOR"));
        priv.setName(result.getStringResult("TABLE_NAME"));
        priv.setPrivilege(result.getStringResult("PRIVILEGE"));
        priv.setGrantable(result.getStringResult("GRANTABLE"));
        return priv;
    }
}
