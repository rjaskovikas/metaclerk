package org.rola.metaclerk.model.diff;

import org.rola.metaclerk.model.TableDescription;

import java.util.ArrayList;

public class TableDiffList extends ArrayList<TableDifference> {

    public void addExpectedTableNotFoundDifference(TableDescription expectedTable) {
        TableDifference tblDiff = new TableDifference();
        tblDiff.setExpectedTable(expectedTable);
        tblDiff.addTableDifference(TableDiffType.EXPECTED_NOT_FOUND);
        this.add(tblDiff);
    }

    public void addNewActualTableDifference(TableDescription actualTable) {
        TableDifference tblDiff = new TableDifference();
        tblDiff.setActualTable(actualTable);
        tblDiff.addTableDifference(TableDiffType.ACTUAL_NEW);
        this.add(tblDiff);
    }

//    public boolean hasDifferences() {
//        return this.size() > 0;
//    }
}
