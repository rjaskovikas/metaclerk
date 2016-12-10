package org.rola.metaclerk.model.diff;


import org.rola.metaclerk.model.ColumnDescription;

import java.util.ArrayList;
import java.util.List;

public class ColumnDifference {
    private final ColAttrDiffSet attrDiffSet = new ColAttrDiffSet();
    private final List<String> warnings = new ArrayList<>();

    private ColumnDescription expected;
    private ColumnDescription actual;


    @SuppressWarnings("SameReturnValue")
    public DiffType getDiffType() {
        return DiffType.COLUMN_DIFF;
    }

    public void addColumnDiff(ColumnDiffType type) {
        attrDiffSet.add(type);
    }

    public void removeColumnDiff(ColumnDiffType diffType) {
        attrDiffSet.remove(diffType);
    }

    public void addWarning(String warningMessage) {
        warnings.add(warningMessage);
    }

//    public ColAttrDiffSet getAttributeDifferences() {
//        return attrDiffSet;
//    }


    public boolean hasDifferences() {
        return attrDiffSet.size() > 0;
    }

    public boolean hasWarnings() {
        return warnings.size() > 0;
    }

    public boolean hasDifference(ColumnDiffType type) {
        return attrDiffSet.contains(type);
    }

    public ColumnDescription getExpectedCol() {
        return expected;
    }

    public void setExpectedCol(ColumnDescription expected) {
        this.expected = expected;
    }

    public ColumnDescription getActualCol() {
        return actual;
    }

    public void setActualCol(ColumnDescription actual) {
        this.actual = actual;
    }

    public List<String> getWarnings () {
        return warnings;
    }

    public String getColumnName() {
        if (expected != null) return expected.getName();
        if (actual != null) return actual.getName();
        return "Unknown";
    }
}
