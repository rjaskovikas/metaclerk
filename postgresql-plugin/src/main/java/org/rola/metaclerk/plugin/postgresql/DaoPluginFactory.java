package org.rola.metaclerk.plugin.postgresql;

import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.plugin.api.DaoFactory;
import org.rola.metaclerk.plugin.postgresql.dao.impl.PGSqlJdbcConnectorImpl;
import org.rola.metaclerk.plugin.postgresql.dao.impl.PGSqlSchemaDAOImpl;

public class DaoPluginFactory implements DaoFactory {
    @Override
    public AllSchemaObjectsDAO createAllSchemaObjectsDAO() {
        PGSqlJdbcConnectorImpl dbc = new PGSqlJdbcConnectorImpl();
        return new PGSqlSchemaDAOImpl(dbc);
    }
}
