package org.rola.metaclerk.plugin.mysql;

import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.plugin.api.DaoFactory;
import org.rola.metaclerk.plugin.mysql.dao.impl.MySqlJdbcConnectorImpl;
import org.rola.metaclerk.plugin.mysql.dao.impl.MySqlSchemaDAOImpl;

public class DaoPluginFactory implements DaoFactory {
    @Override
    public AllSchemaObjectsDAO createAllSchemaObjectsDAO() {
        MySqlJdbcConnectorImpl dbc = new MySqlJdbcConnectorImpl();
        return new MySqlSchemaDAOImpl(dbc);
    }
}
