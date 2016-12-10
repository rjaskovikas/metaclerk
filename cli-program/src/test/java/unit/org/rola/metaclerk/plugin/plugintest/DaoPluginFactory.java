package org.rola.metaclerk.plugin.plugintest;

import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.DbSchemas;
import org.rola.metaclerk.plugin.api.DaoFactory;

public class DaoPluginFactory implements DaoFactory {
    @Override
    public AllSchemaObjectsDAO createAllSchemaObjectsDAO() {
        return new AllSchemaObjectsDAO() {

            @Override
            public DbSchema getAllSchemaObjects(String schemaName) {
                return DbSchemas.createDbSchema("PluginTest-" + schemaName);
            }

            @Override
            public void initDAO(String jdbcConnectionStr, String dbUser, String dbPassword) {

            }

            @Override
            public void closeDAO() {

            }
        };

    }
}
