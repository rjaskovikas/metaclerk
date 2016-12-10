package org.rola.metaclerk.comparator.api;

import org.rola.metaclerk.model.PrivilegeList;
import org.rola.metaclerk.model.diff.SchemaDifference;

public interface PrivilegeListComparator {
    boolean compareEqual(PrivilegeList expectedList, PrivilegeList actuaList, SchemaDifference schemaDiff);
}
