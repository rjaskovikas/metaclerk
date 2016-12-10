package org.rola.metaclerk.comparator.api;

import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.diff.TableDifference;


public interface TableComparator {
    boolean isTablesEqual();

    boolean compareEqual(TableDescription expected, TableDescription actual);

    TableDifference getTableDifference();
}
