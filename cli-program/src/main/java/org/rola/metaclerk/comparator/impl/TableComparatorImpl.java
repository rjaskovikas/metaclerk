package org.rola.metaclerk.comparator.impl;

import org.rola.metaclerk.comparator.api.ColumnComparator;
import org.rola.metaclerk.comparator.api.TableComparator;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.diff.TableDifference;

public class TableComparatorImpl implements TableComparator {
    private TableDescription expectedTable;
    private TableDescription actualTable;
    ColumnComparator colComp = new ColumnComparatorImpl();
    private TableDifference tableDiff;

    @Override
    public boolean isTablesEqual() {
        return !tableDiff.hasDifferences();
    }

    @Override
    public boolean compareEqual(TableDescription expected, TableDescription actual) {
        init(expected, actual);
        compareTables();
        return isTablesEqual();
    }

    @Override
    public TableDifference getTableDifference() {
        return tableDiff;
    }

    private void init(TableDescription expected, TableDescription actual) {
        this.expectedTable = expected;
        this.actualTable = actual;
        tableDiff = new TableDifference();
        tableDiff.setActualTable(actual);
        tableDiff.setExpectedTable(expected);
    }

    private void compareTables() {
        compareName();
        compareColumns();
    }

    private void compareColumns() {
        actualTable.getColumns().fillWasFound(false);
        expectedTable.getColumns().forEach(this::lookForActualColumnAndCheckEqual);
        checkForNewActualColumns();
    }

    private void checkForNewActualColumns() {
        actualTable.getColumns().forEach(col-> {
            if (!col.isWasFound())
                tableDiff.addNewActualColumnDifference(col);
        });
    }

    private void lookForActualColumnAndCheckEqual(ColumnDescription expCol) {
        ColumnDescription actCol = findActualColumnByName(expCol.getName());
        if (actCol == null)
            tableDiff.addExpectedColumnNotFoundDifference(expCol);
        else {
            actCol.setWasFound(true);
            checkColumnsAreEqual(expCol, actCol);
        }
    }


    private void checkColumnsAreEqual(ColumnDescription expC, ColumnDescription actC) {
        if (!colComp.compareEqual(expC, actC)) {
            tableDiff.addColumnDifference(colComp.getColumnDifference());
        }
    }

    private ColumnDescription findActualColumnByName(String name) {
        return actualTable.getColumns().findByName(name);
    }

    private void compareName() {
        if (!expectedTable.getName().equalsIgnoreCase(actualTable.getName()))
            tableDiff.addTableNameDifference();
    }
}
