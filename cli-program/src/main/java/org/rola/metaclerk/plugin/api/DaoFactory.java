package org.rola.metaclerk.plugin.api;

import org.rola.metaclerk.dao.api.AllSchemaObjectsDAO;

public interface DaoFactory {
    AllSchemaObjectsDAO createAllSchemaObjectsDAO();
}
