package org.rola.metaclerk.comparator.impl;


import org.rola.metaclerk.comparator.api.PrivilegeListComparator;
import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.model.PrivilegeList;
import org.rola.metaclerk.model.diff.PrivilegeDiffType;
import org.rola.metaclerk.model.diff.PrivilegeDifference;
import org.rola.metaclerk.model.diff.SchemaDifference;

public class PrivilegeListComparatorImpl implements PrivilegeListComparator {

    private PrivilegeList expectedList;
    private PrivilegeList actualList;
    private SchemaDifference schemaDiff;

    @Override
    public boolean compareEqual(PrivilegeList expectedList, PrivilegeList actualList, SchemaDifference schemaDiff) {
        init(expectedList, actualList, schemaDiff);
        comparePrivilegesLists();
        return !schemaDiff.hasPrivilegeDifferences();
    }

    private void init(PrivilegeList expected, PrivilegeList actual, SchemaDifference schemaDiff) {
        this.expectedList = expected;
        this.actualList = actual;
        this.schemaDiff = schemaDiff;
    }

    private void comparePrivilegesLists() {
        actualList.fillWasFound(false);
        expectedList.forEach(this::lookForAndComparePrivilege);
        fillActualNew();
    }

    private void fillActualNew() {
        actualList.forEach(actual-> {
            if (!actual.isWasFound())
                schemaDiff.addNewActualPrivilegeDifference(actual);
        });
    }

    private void lookForAndComparePrivilege(DbPrivilege expected) {
        DbPrivilege actual = actualList.findPrivilege(expected);
        if (actual == null) schemaDiff.addExpectedPrivilegeNotFoundDifference(expected);
        else  {
            actual.setWasFound(true);
            comparePrivilegesEqual(expected, actual);
        }
    }

    private void comparePrivilegesEqual(DbPrivilege expected, DbPrivilege actual) {
        if (!expected.getGrantable().equalsIgnoreCase(actual.getGrantable())) {
            schemaDiff.addPrivilegeDifference(new PrivilegeDifference(expected, actual, PrivilegeDiffType.GRANT));
        }
    }
}
