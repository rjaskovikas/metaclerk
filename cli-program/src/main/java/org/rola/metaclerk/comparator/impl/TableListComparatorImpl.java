package org.rola.metaclerk.comparator.impl;

import org.rola.metaclerk.comparator.api.TableComparator;
import org.rola.metaclerk.comparator.api.TableListComparator;
import org.rola.metaclerk.model.DbObject;
import org.rola.metaclerk.model.DbObjectList;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.diff.SchemaDifference;

import java.util.List;

class TableListComparatorImpl implements TableListComparator {

    private DbObjectList<DbObject> actual;
    private DbObjectList<DbObject> expected;
    private List<String> ignoreTableList;
    private SchemaDifference schemaDiff;

    TableComparator tblComp = new TableComparatorImpl();

    @Override
    public void compareEqual(DbObjectList expected, DbObjectList actual, List<String> ignoreTableList, SchemaDifference schemaDiff) {
        init(expected, actual, ignoreTableList, schemaDiff);
        removeTablesToIgnoreFromTableList(this.actual);
        removeTablesToIgnoreFromTableList(this.expected);
        compareTablesByAttributes();
    }

    @SuppressWarnings("unchecked")
    private void init(DbObjectList expected, DbObjectList actual, List<String> ignoreTableList, SchemaDifference schemaDiff) {
        this.actual = actual;
        this.expected = expected;
        this.ignoreTableList = ignoreTableList;
        this.schemaDiff = schemaDiff;
    }


    private void removeTablesToIgnoreFromTableList(DbObjectList list) {
        for (String tableName : ignoreTableList) {
            DbObject table = list.findByName(tableName);
            if (table != null) list.remove(table);
        }
    }

    private void compareTablesByAttributes() {
        expected.fillWasFound(false);
        expected.forEach(this::findActualTableAndCompareEqualTo);
        checkForNotFoundActualTables();
    }

    private void checkForNotFoundActualTables() {
        actual.forEach(actTable -> {
            if (!actTable.isWasFound())
                schemaDiff.addNewActualTableDifference((TableDescription) actTable);
        });
    }

    private void findActualTableAndCompareEqualTo(DbObject expectedTable) {
        DbObject tAc = findActualTableByName(expectedTable.getName());
        if (tAc == null) {
            schemaDiff.addExpectedTableNotFoundDifference((TableDescription) expectedTable);
            return;
        }
        if (!tblComp.compareEqual((TableDescription) expectedTable, (TableDescription) tAc))
            schemaDiff.addTableDifference(tblComp.getTableDifference());
        tAc.setWasFound(true);
    }

    private DbObject findActualTableByName(String tableName) {
        return actual.findByName(tableName);
    }
}