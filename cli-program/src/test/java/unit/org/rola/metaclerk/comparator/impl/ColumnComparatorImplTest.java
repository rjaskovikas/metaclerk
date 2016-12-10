package org.rola.metaclerk.comparator.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.Columns;
import org.rola.metaclerk.model.diff.ColumnDiffType;

import static org.junit.Assert.*;

public class ColumnComparatorImplTest {

    private ColumnComparatorImpl cmp;
    private ColumnDescription a;
    private ColumnDescription b;

    @Test(expected = NullPointerException.class)
    public void nullColumns_compare_throwsNullPointerException() throws Exception {
        cmp.compareEqual(null, null);
    }

    @Before
    public void setUp() throws Exception {
        cmp = new ColumnComparatorImpl();
    }

    @Test
    public void nullPrintStream_compare_worksWithoutPrinting() throws Exception {
        assertTrue(cmp.compareEqual(Columns.createNUMBERColumn(1, "vienas"), Columns.createNUMBERColumn(1,"vienas")));
        assertFalse(cmp.compareEqual(Columns.createNUMBERColumn(1, "vienas"), Columns.createNVARCHAR2Column(1,"du")));
    }

    @Test
    public void columnsWithDiffType_compare_returnsFalse() throws Exception {
        ColumnDescription a = Columns.createNUMBERColumn(1, "vienas");
        ColumnDescription b = Columns.createNVARCHAR2Column(1, "vienas");

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.TYPE));
    }

    @Test
    public void columnsWithDiffLength_compare_returnsFalse() throws Exception {
        createEqualColumnsWithCheck();
        b.setDataLength(a.getDataLength()+1);

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.LENGTH));
    }

    @Test
    public void columnsWithDiffScale_compare_returnsFalse() throws Exception {
        createEqualColumnsWithCheck();
        b.setDataScale(a.getDataScale()+1);

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.SCALE));
    }

    @Test
    public void columnsWithDiffPrecision_compare_returnFalse() throws Exception {
        createEqualColumnsWithCheck();
        b.setDataPrecision(a.getDataPrecision()+1);

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.PRECISION));
    }

    @Test
    public void columnsWithDiffDefault_compare_returnFalse() throws Exception {
        createEqualColumnsWithCheck();
        b.setDataDefault(a.getDataDefault()+"a");

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.DATA_DEFAULT));
    }

    @Test
    public void columnsWithSameDefaultsButInDifferentCaseAndWithExtraSpaces_compare_returnTrue() throws Exception {
        createEqualColumnsWithCheck();
        a.setDataDefault("NULL\n\r");
        b.setDataDefault("null ");

        assertTrue(cmp.compareEqual(a, b));
        assertFalse(cmp.getColumnDifference().hasDifference(ColumnDiffType.DATA_DEFAULT));
    }

    @Test
    public void columnsWithDiffDefaultNullsCases_compare_returnTrueAndAdsWarning() throws Exception {
        createEqualColumnsWithCheck();
        a.setDataDefault(null);
        b.setDataDefault("NULL\n");

        assertTrue(cmp.compareEqual(a, b));
        assertFalse(cmp.getColumnDifference().hasDifference(ColumnDiffType.DATA_DEFAULT));
        assertTrue(cmp.getColumnDifference().hasWarnings());
        assertEquals(1, cmp.getColumnDifference().getWarnings().size());
    }

    @Test
    public void columnsWithDiffCharUsed_compare_returnFalse() throws Exception {
        createEqualColumnsWithCheck();
        b.setCharUsed(a.getCharUsed()+"a");

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.CHAR_USED));
    }

    @Test
    public void columnsWithDiffNullable_compare_returnFalse() throws Exception {
        createEqualColumnsWithCheck();
        b.setNullable(!a.isNullable());

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.NULLABLE));
    }

    @Test
    public void columnWithValueAndNullColumn_compare_returnFalse() throws Exception {
        createEqualColumnsWithCheck();
        b.setDataPrecision(null);

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.PRECISION));
    }

    @Test
    public void columnsWithNullValue_compare_returnFalse() throws Exception {
        createEqualColumnsWithCheck();
        a.setDataPrecision(null);
        b.setDataPrecision(null);

        assertTrue(cmp.compareEqual(a, b));
        assertFalse(cmp.getColumnDifference().hasDifference(ColumnDiffType.PRECISION));
    }

    @Test
    public void twoChecksInSequenceFirstWithErrorSecondNoError_compare_firstReturnsFalseSecondTrue() throws Exception {
        createEqualColumnsWithCheck();
        Integer tmp = a.getDataPrecision();
        a.setDataPrecision(null);

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.PRECISION));

        a.setDataPrecision(tmp);
        assertTrue(cmp.compareEqual(a, b));
        assertFalse(cmp.getColumnDifference().hasDifference(ColumnDiffType.PRECISION));
    }

    @Test
    public void nullColumnAndColumnWithValue_compare_returnFalse() throws Exception {
        createEqualColumnsWithCheck();
        a.setDataPrecision(null);

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getColumnDifference().hasDifference(ColumnDiffType.PRECISION));
    }

    private void createEqualColumnsWithCheck() {
        a = Columns.createNUMBERColumn(1, "vienas");
        b = Columns.createNUMBERColumn(1, "vienas");

        assertTrue(cmp.compareEqual(a, b));
    }
}