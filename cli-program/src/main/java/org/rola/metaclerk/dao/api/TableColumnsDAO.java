package org.rola.metaclerk.dao.api;

import org.rola.metaclerk.model.TableDescription;

public interface TableColumnsDAO extends DAOBaseOperations {
    void fillTableColumnDescription(TableDescription table);
}
