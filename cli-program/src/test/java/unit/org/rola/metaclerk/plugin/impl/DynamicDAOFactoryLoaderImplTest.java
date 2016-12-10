package org.rola.metaclerk.plugin.impl;

import org.junit.Test;
import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.plugin.api.DaoFactory;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;

import static org.junit.Assert.*;

public class DynamicDAOFactoryLoaderImplTest {

    private DynamicDAOFactoryLoader loader = new DynamicDAOFactoryLoaderImpl();

    @Test(expected = DAOException.class)
    public void nullAsPluginName_load_throwsException() throws Exception {
        loader.loadDaoPluginFactory(null);
    }

    @Test
    public void plugintestAsPluginName_load_loadsTestPluginFactory() throws Exception {
        DaoFactory daoFactory = loader.loadDaoPluginFactory("plugintest");
        AllSchemaObjectsDAO dao = daoFactory.createAllSchemaObjectsDAO();
        DbSchema schema = dao.getAllSchemaObjects("Test");

        assertNotNull(schema);
        assertEquals("PluginTest-Test", schema.getName());
    }

    @Test
    public void plugintestAsPluginNameInMixedCase_load_loadsTestPluginFactory() throws Exception {
        DaoFactory daoFactory = loader.loadDaoPluginFactory("PlUgInTeSt");
        AllSchemaObjectsDAO dao = daoFactory.createAllSchemaObjectsDAO();
        DbSchema schema = dao.getAllSchemaObjects("TestNoCase");

        assertNotNull(schema);
        assertEquals("PluginTest-TestNoCase", schema.getName());
    }

    @Test
    public void nonExistingPlunginName_load_throwsDAOExceptionWithClassNoFoundExceptionAsCause() throws Exception {
        try {
            loader.loadDaoPluginFactory("NoExistingPlugin");
        } catch (DAOException ex) {
            assertTrue(ex.getCause() instanceof ClassNotFoundException);
        }
    }
}