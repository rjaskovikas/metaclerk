package org.rola.metaclerk.comparator.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.comparator.api.TableComparator;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.TableList;
import org.rola.metaclerk.model.Tables;
import org.rola.metaclerk.model.diff.SchemaDiffType;
import org.rola.metaclerk.model.diff.SchemaDifference;
import org.rola.metaclerk.model.diff.TableDifference;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TableListComparatorTest {
    private final TableListComparatorImpl cmp = new TableListComparatorImpl();
    private TableList a;
    private TableList b;
    private final List<String> ignoreList = new ArrayList<>();
    private boolean tableComparatorReturnValue = true;
    private final SchemaDifference schemaDiff = new SchemaDifference();

    @Before
    public void setUp() throws Exception {
        cmp.tblComp = new TableComparator() {
            @Override
            public boolean isTablesEqual() {
                return tableComparatorReturnValue;
            }

            @Override
            public boolean compareEqual(TableDescription expected, TableDescription actual) {
                return tableComparatorReturnValue;
            }

            @Override
            public TableDifference getTableDifference() {
                return new TableDifference();
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void nulls_compare_throwsNullPointerException() throws Exception {
        cmp.compareEqual(null, null, ignoreList, schemaDiff);
    }

    @Test
    public void equalTableLists_compare_schemaDiffHasNoDifferences() throws Exception {
        prepareEqualTableLists();
    }

    @Test
    public void tableListWithIgnoredTableInTableListA_compare_schemaDiffHasNoDifferences() throws Exception {
        prepareEqualTableLists();
        a.add(Tables.createTableWithColumns("ignore", "schema"));
        int tableCount = a.size();
        ignoreList.add("ignore");

        cmp.compareEqual(a, b, ignoreList, schemaDiff);
        assertFalse(schemaDiff.hasDifferences());
        assertEquals(tableCount-1, a.size()); // ignored tables removed from TableList
    }

    @Test
    public void tableListsWithIgnoredTableInTableListB_compare_schemaDiffHasNoDifferences() throws Exception {
        prepareEqualTableLists();
        b.add(Tables.createTableWithColumns("ignore", "schema"));
        int tableCount = b.size();
        ignoreList.add("ignore");

        cmp.compareEqual(a, b, ignoreList, schemaDiff);
        assertFalse(schemaDiff.hasDifferences());
        assertEquals(tableCount-1, b.size()); // ignored tables removed from TableList
    }

    @Test
    public void TableListsWithDiffTableNumber_compare_schemaDiffHasDifferences() throws Exception {
        prepareEqualTableLists();
        a.add(Tables.createTableWithColumns("new_in_expected", "a"));
        b.add(Tables.createTableWithColumns("new_in_actual", "a"));

        cmp.compareEqual(a, b, ignoreList, schemaDiff);
        assertTrue(schemaDiff.hasDifferences());
        assertTrue(schemaDiff.hasDifference(SchemaDiffType.TABLES_DIFF));
    }

    @Test
    public void tableListsWithDiffTableColumns_compare_schemaDiffHasDifferences() throws Exception {
        prepareEqualTableLists();
        tableComparatorReturnValue = false; // imitate different table columns

        cmp.compareEqual(a, b, ignoreList, schemaDiff);
        assertTrue(schemaDiff.hasDifferences());
        assertTrue(schemaDiff.hasDifference(SchemaDiffType.TABLES_DIFF));
    }

    private void prepareEqualTableLists() {
        a = Tables.createTables("a", "Vienas", "Du");
        b = Tables.createTables("b", "Vienas", "Du");

        cmp.compareEqual(a, b, ignoreList, schemaDiff);
        assertFalse(schemaDiff.hasDifferences());
    }

}