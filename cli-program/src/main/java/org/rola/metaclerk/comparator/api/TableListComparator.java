package org.rola.metaclerk.comparator.api;

import org.rola.metaclerk.model.DbObjectList;
import org.rola.metaclerk.model.diff.SchemaDifference;

import java.util.List;

public interface TableListComparator {
    void compareEqual(DbObjectList expected, DbObjectList actual, List<String> ignoreTableList, SchemaDifference schemaDiff);
}
