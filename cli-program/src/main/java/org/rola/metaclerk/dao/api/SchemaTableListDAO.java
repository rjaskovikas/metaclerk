package org.rola.metaclerk.dao.api;

import org.rola.metaclerk.model.TableList;

public interface SchemaTableListDAO extends DAOBaseOperations {
    TableList getSchemaTableList(String schema);
}
