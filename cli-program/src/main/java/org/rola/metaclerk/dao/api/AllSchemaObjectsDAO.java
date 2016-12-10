package org.rola.metaclerk.dao.api;

import org.rola.metaclerk.model.DbSchema;

public interface AllSchemaObjectsDAO extends DAOBaseOperations {
    DbSchema getAllSchemaObjects(String schemaName);
}
