package org.rola.metaclerk.model.diff;


import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.model.TableDescription;

import java.util.HashSet;
import java.util.Set;

public class SchemaDifference {
    private final TableDiffList tableDifferenceList = new TableDiffList();
    private final PrivilegeDiffList privilegeDiffList = new PrivilegeDiffList();

    private final Set<SchemaDiffType> diffSet = new HashSet<>();

    public TableDiffList getTableDifferenceList() {
        return tableDifferenceList;
    }

    public void addTableDifference(TableDifference tableDiff) {
        tableDifferenceList.add(tableDiff);
        diffSet.add(SchemaDiffType.TABLES_DIFF);
    }

    public boolean hasDifferences() {
        return diffSet.size() > 0;
    }

    public boolean hasTableDifferences() {
        return this.diffSet.contains(SchemaDiffType.TABLES_DIFF);
    }

    public boolean hasPrivilegeDifferences() {
        return this.diffSet.contains(SchemaDiffType.PRIVILEGE_DIFF);
    }

    public void addNewActualTableDifference(TableDescription actualTableDiff) {
        tableDifferenceList.addNewActualTableDifference(actualTableDiff);
        diffSet.add(SchemaDiffType.TABLES_DIFF);
    }

    public void addExpectedTableNotFoundDifference(TableDescription expectedTableDiff) {
        tableDifferenceList.addExpectedTableNotFoundDifference(expectedTableDiff);
        diffSet.add(SchemaDiffType.TABLES_DIFF);
    }

    public void addExpectedPrivilegeNotFoundDifference(DbPrivilege expectedPrivilege) {
        privilegeDiffList.addExpectedPrivilegeNotFoundDifference(expectedPrivilege);
        diffSet.add(SchemaDiffType.PRIVILEGE_DIFF);
    }

    public void addNewActualPrivilegeDifference(DbPrivilege actualPrivilege) {
        privilegeDiffList.addNewActualPrivilegeDifference(actualPrivilege);
        diffSet.add(SchemaDiffType.PRIVILEGE_DIFF);
    }

    public void addPrivilegeDifference(PrivilegeDifference diff) {
        privilegeDiffList.add(diff);
        diffSet.add(SchemaDiffType.PRIVILEGE_DIFF);
    }

    public boolean hasDifference(SchemaDiffType diffType) {
        return diffSet.contains(diffType);
    }

    public PrivilegeDiffList getPrivilegeDifferences() {
        return privilegeDiffList;
    }
}
