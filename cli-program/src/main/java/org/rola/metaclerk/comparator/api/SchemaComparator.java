package org.rola.metaclerk.comparator.api;


import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.diff.SchemaDifference;

import java.util.List;

public interface SchemaComparator {
    boolean compareEqual(DbSchema expected, DbSchema actual);

    boolean isSchemasEqual();

    void setIgnoreTableList(List<String> ignoreTableList);

    SchemaDifference getSchemaDifference();
}
