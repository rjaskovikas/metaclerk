package org.rola.metaclerk.plugin.oracle;

import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.plugin.api.DaoFactory;
import org.rola.metaclerk.plugin.oracle.dao.impl.OracleJdbcConnectorImpl;
import org.rola.metaclerk.plugin.oracle.dao.impl.OracleSchemaDAOImpl;


public class DaoPluginFactory implements DaoFactory {
    @Override
    public AllSchemaObjectsDAO createAllSchemaObjectsDAO() {
        OracleJdbcConnectorImpl dbc = new OracleJdbcConnectorImpl();
        return new OracleSchemaDAOImpl(dbc);
    }
}
