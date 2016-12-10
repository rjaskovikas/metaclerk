package org.rola.metaclerk.model.diff;


import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.TableDescription;

public class TableDifference {
    private final ColumnDifferenceList columnDifferenceList = new ColumnDifferenceList();
    private TableDescription expectedTable;
    private TableDescription actualTable;
    private final TableAttributeDiffSet diffSet = new TableAttributeDiffSet();
    private String tableName;

    public void addColumnDifference (ColumnDifference colDiff) {
        columnDifferenceList.add(colDiff);
        diffSet.add(TableDiffType.COLUMNS);
    }

    public void addTableNameDifference () {
        diffSet.add(TableDiffType.NAME);
    }

    public void addExpectedColumnNotFoundDifference(ColumnDescription expectedColumn) {
        ColumnDifference colDif = new ColumnDifference();
        colDif.setExpectedCol(expectedColumn);
        colDif.addColumnDiff(ColumnDiffType.EXPECTED_NOT_FOUND);
        addColumnDifference(colDif);
    }

    public void addNewActualColumnDifference(ColumnDescription actualColumn) {
        ColumnDifference colDif = new ColumnDifference();
        colDif.setActualCol(actualColumn);
        colDif.addColumnDiff(ColumnDiffType.ACTUAL_NEW);
        addColumnDifference(colDif);
    }

    public ColumnDifferenceList getColumnDifferenceList() {
        return columnDifferenceList;
    }

    public TableDescription getExpectedTable() {
        return expectedTable;
    }

    public void setExpectedTable(TableDescription expectedTable) {
        this.expectedTable = expectedTable;
    }

    public TableDescription getActualTable() {
        return actualTable;
    }

    public void setActualTable(TableDescription actualTable) {
        this.actualTable = actualTable;
    }

    public boolean hasDifferences() {
        return diffSet.size() > 0;
    }

    public boolean hasDifference(TableDiffType diffType) {
        return diffSet.contains(diffType);
    }

    public void addTableDifference(TableDiffType diffType) {
        diffSet.add(diffType);
    }

    public boolean hasWarnings() {
        for (ColumnDifference d : getColumnDifferenceList())
            if (d.hasWarnings()) return true;
        return false;
    }

    public String getTableName() {
        if (expectedTable != null) return expectedTable.getName();
        if (actualTable != null) return actualTable.getName();
        return "Unknown";
    }
}
