package org.rola.metaclerk.comparator.impl;

import org.rola.metaclerk.comparator.api.ColumnComparator;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.diff.ColumnDiffType;
import org.rola.metaclerk.model.diff.ColumnDifference;

public class ColumnComparatorImpl implements ColumnComparator {
    private ColumnDescription expectedCol;
    private ColumnDescription actualCol;
    private ColumnDifference colDiff;

    @Override
    public boolean compareEqual(ColumnDescription expected, ColumnDescription actual) {
        init(expected, actual);
        compareAttribute();
        replaceErrorsWithWarnings();
        return !colDiff.hasDifferences();
    }

    @Override
    public ColumnDifference getColumnDifference() {
        return colDiff;
    }


    private void init(ColumnDescription expected, ColumnDescription actual) {
        this.expectedCol = expected;
        this.actualCol = actual;
        colDiff = new ColumnDifference();
        colDiff.setActualCol(actual);
        colDiff.setExpectedCol(expected);
    }

    private void compareAttribute() {
        compareName();
        compareType();
        compareDataLength();
        compareDataPrecision();
        compareDataScale();
        compareDataDefault();
        compareCharUsed();
        compareNullable();
    }

    private void compareNullable() {
        compareAttribute(expectedCol.isNullable(), actualCol.isNullable(), ColumnDiffType.NULLABLE);
    }

    private void compareDataDefault() {
         compareStringAttributes(expectedCol.getDataDefault(), actualCol.getDataDefault(), ColumnDiffType.DATA_DEFAULT);
    }

    private void compareCharUsed() {
         compareStringAttributes(expectedCol.getCharUsed(), actualCol.getCharUsed(), ColumnDiffType.CHAR_USED);
    }

    private void compareDataScale() {
         compareAttribute(expectedCol.getDataScale(), actualCol.getDataScale(), ColumnDiffType.SCALE);
    }

    private void compareDataPrecision() {
         compareAttribute(expectedCol.getDataPrecision(), actualCol.getDataPrecision(), ColumnDiffType.PRECISION);
    }

    private void compareDataLength() {
         compareAttribute(expectedCol.getDataLength(), actualCol.getDataLength(), ColumnDiffType.LENGTH);
    }

    private void compareType() {
         compareStringAttributes(expectedCol.getType(), actualCol.getType(), ColumnDiffType.TYPE);
    }

    private void compareName() {
         compareStringAttributes(expectedCol.getName(), actualCol.getName(), ColumnDiffType.NAME);
    }

    private void compareStringAttributes(String a, String b, ColumnDiffType diffType) {
        compareAttribute(equalize(a), equalize(b), diffType);
    }

    static private Object equalize(String value) {
        //noinspection ConstantConditions
        return value == null ? value : value.trim().toUpperCase();
    }

    private void compareAttribute(Object a, Object b, ColumnDiffType diffType) {
        if (a == b || (a != null && a.equals(b)))
            return;
        colDiff.addColumnDiff(diffType);
    }

    private void replaceErrorsWithWarnings() {
        replaceDefaultDataNullValueDifferenceWithWarning();
    }

    private void replaceDefaultDataNullValueDifferenceWithWarning() {
        // It is possible to specify data default 'null' value for table column in two ways:
        //   1. specify explicitly that data default is null e.g. column_name varchar(2) default NULLL;
        //   2. simply omit default keyword in column description;
        // semantically both ways are the same, but meta data will be different. Therefore comparator will flag an
        // difference that must be replaced with warning message
        if (colDiff.hasDifference(ColumnDiffType.DATA_DEFAULT)) {
            String a = expectedCol.getDataDefault() == null ? "null" : expectedCol.getDataDefault().trim();
            String b = actualCol.getDataDefault() == null ? "null" : actualCol.getDataDefault().trim();
            if (a.equalsIgnoreCase(b))
                colDiff.removeColumnDiff(ColumnDiffType.DATA_DEFAULT);
                colDiff.addWarning("Different 'data default' null values");
        }
    }
}
