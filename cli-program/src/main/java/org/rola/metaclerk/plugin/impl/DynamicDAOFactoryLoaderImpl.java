package org.rola.metaclerk.plugin.impl;


import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.plugin.api.DaoFactory;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;

public class DynamicDAOFactoryLoaderImpl implements DynamicDAOFactoryLoader {

    private static final String PLUGIN_LOAD_PATTERN = "org.rola.metaclerk.plugin.%s.DaoPluginFactory";

    @Override
    public DaoFactory loadDaoPluginFactory(String dbType) {
        throwExceptionIfDbTypeIsNull(dbType);
        String daoPluginFullName = String.format(PLUGIN_LOAD_PATTERN, dbType.toLowerCase());
        return loadDaoPluginFactorySafe(daoPluginFullName);
    }

    private void throwExceptionIfDbTypeIsNull(String dbType) {
        if (dbType == null)
            throw new DAOException("Error loading DAO plugin: unknown db type", null);
    }

    private DaoFactory loadDaoPluginFactorySafe(String daoPluginFullName) {
        DaoFactory factory;
        try {
            factory = (DaoFactory) this.getClass().getClassLoader().loadClass(daoPluginFullName).newInstance();
            return factory;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            throw new DAOException("Error loading DAO plugin", ex);
        }
    }
}
