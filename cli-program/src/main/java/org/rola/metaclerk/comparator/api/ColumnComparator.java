package org.rola.metaclerk.comparator.api;

import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.diff.ColumnDifference;

public interface ColumnComparator {
    boolean compareEqual(ColumnDescription expected, ColumnDescription actual);

    ColumnDifference getColumnDifference();
}
