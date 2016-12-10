package org.rola.metaclerk.comparator.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.comparator.api.ColumnComparator;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.Columns;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.Tables;
import org.rola.metaclerk.model.diff.ColumnDifference;
import org.rola.metaclerk.model.diff.TableDiffType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TableComparatorImplTest {

    private final TableComparatorImpl cmp = new TableComparatorImpl();
    private TableDescription a;
    private TableDescription b;
    private boolean compareColumnsReturnValue = true;

    @Before
    public void setUp() throws Exception {
        cmp.colComp = new ColumnComparator() {
            @Override
            public boolean compareEqual(ColumnDescription expected, ColumnDescription actual) {
                return compareColumnsReturnValue;
            }

            @Override
            public ColumnDifference getColumnDifference() {
                return null;
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void null_compare_nullPointerException() throws Exception {
        cmp.compareEqual(null, null);
    }

    @Test
    public void equalTables_compare_returnsTrue() throws Exception {
        createEqualTables();
    }

    @Test
    public void tablesWithDiffName_compare_returnFalse() throws Exception {
        createEqualTables();
        a.setName(b.getName()+"a");

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getTableDifference().hasDifference(TableDiffType.NAME));
    }

    @Test
    public void tableWithDiffColName_compare_returnFalse() throws Exception {
        createEqualTables();
        a.getColumns().get(0).setName("aa");
        compareColumnsReturnValue = false; //imitate columns are different

        assertFalse(cmp.compareEqual(a, b));
        assertTrue(cmp.getTableDifference().hasDifference(TableDiffType.COLUMNS));
    }

    @Test
    public void tableAWithDiffColNumber_compare_returnFalse() throws Exception {
        createEqualTables();
        a.getColumns().add(Columns.createNVARCHAR2Column(10, "additional"));

        assertFalse(cmp.compareEqual(a, b));
    }

    @Test
    public void tableBWithDiffColNumber_compare_returnFalse() throws Exception {
        createEqualTables();
        b.getColumns().add(Columns.createNVARCHAR2Column(10, "additional"));

        assertFalse(cmp.compareEqual(a, b));
    }
    private void createEqualTables() {
        a = Tables.createTableWithColumns();
        b = Tables.createTableWithColumns();

        assertTrue(cmp.compareEqual(a, b));
    }
}