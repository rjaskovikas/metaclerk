package org.rola.metaclerk.model.diff;

import org.rola.metaclerk.model.DbPrivilege;

import java.util.ArrayList;

public class PrivilegeDiffList extends ArrayList<PrivilegeDifference> {

    public boolean hasDifference() {
        return this.size() > 0;
    }

    public void addExpectedPrivilegeNotFoundDifference(DbPrivilege expectedPrivilege) {
        PrivilegeDifference diff = new PrivilegeDifference(expectedPrivilege, null, PrivilegeDiffType.EXPECTED_NOT_FOUNT);
        add(diff);
    }

    public void addNewActualPrivilegeDifference(DbPrivilege actualPrivilege) {
        PrivilegeDifference diff = new PrivilegeDifference(null, actualPrivilege, PrivilegeDiffType.ACTUAL_NEW);
        add(diff);
    }
}
