package org.rola.metaclerk.model.diff;


import org.rola.metaclerk.model.DbPrivilege;

public class PrivilegeDifference {
    private DbPrivilege actual;
    private DbPrivilege expected;
    private PrivilegeDiffType difference;

    public PrivilegeDifference( DbPrivilege expected, DbPrivilege actual, PrivilegeDiffType difference) {
        this.actual = actual;
        this.expected = expected;
        this.difference = difference;
    }

    public DbPrivilege getActual() {
        return actual;
    }

    public void setActual(DbPrivilege actual) {
        this.actual = actual;
    }

    public DbPrivilege getExpected() {
        return expected;
    }

    public void setExpected(DbPrivilege expected) {
        this.expected = expected;
    }

    public PrivilegeDiffType getDifference() {
        return difference;
    }

    public void setDifference(PrivilegeDiffType difference) {
        this.difference = difference;
    }
}
