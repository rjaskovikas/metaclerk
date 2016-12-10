package org.rola.metaclerk.plugin.api;


public interface DynamicDAOFactoryLoader {
    DaoFactory loadDaoPluginFactory(String dbType);
}
