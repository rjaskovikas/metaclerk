package org.rola.metaclerk.comparator.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.DbObjectList;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.DbSchemas;
import org.rola.metaclerk.model.PrivilegeList;
import org.rola.metaclerk.model.diff.SchemaDifference;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaComparatorImplTest {

    private final SchemaComparatorImpl cmp = new SchemaComparatorImpl();
    private DbSchema a;
    private DbSchema b;
    private boolean tableComparatorReturnValue = true;
    private final List<DbObjectList> calledExpected = new ArrayList<>();
    private final List<DbObjectList> calledActual = new ArrayList<>();
    private final List<List<String>> calledIgnoreTableList = new ArrayList<>();
    private final List<SchemaDifference> calledSchamaDiff = new ArrayList<>();
    private PrivilegeList privilegeExpected;
    private PrivilegeList privilegeActual;
    private SchemaDifference privilegeSchemaDiff;
    private int privilegeComparatorCallCount;

    @Before
    public void setUp() throws Exception {
        cmp.tableListComparator = (expected, actual, ignoreTableList, schemaDiff) -> {
            SchemaComparatorImplTest.this.calledExpected.add(expected);
            SchemaComparatorImplTest.this.calledActual.add(actual);
            SchemaComparatorImplTest.this.calledIgnoreTableList.add(ignoreTableList);
            SchemaComparatorImplTest.this.calledSchamaDiff.add(schemaDiff);
        };

        cmp.privilegeComparator = (expectedList, actuaList, schemaDiff) -> {
            privilegeExpected = expectedList;
            privilegeActual = actuaList;
            privilegeSchemaDiff = schemaDiff;
            privilegeComparatorCallCount++;
            return true;
        };
    }

    @Test(expected = NullPointerException.class)
    public void nulls_compare_throwsNullPointerException() throws Exception {
        cmp.compareEqual(null, null);
    }


    @Test
    public void noSchemaComparisonWasDone_isSchemaEqual_returnsFalse() throws Exception {
        assertFalse(cmp.isSchemasEqual());
    }


    @Test
    public void equalSchemas_compare_returnsTrue() throws Exception {
        prepareEqualSchemas();
    }

    @Test
    public void schemasWithDiffNames_compare_returnsTrue() throws Exception {
        prepareEqualSchemas();
        a.setName(a.getName()+"a");

        assertTrue(cmp.compareEqual(a, b));
    }

    @Test
    public void schemasWithDbObject_compare_callCompareTableWithCorrectArguments() throws Exception {
        prepareEqualSchemas();
        List<String> ignoreList = new ArrayList<>();
        ignoreList.add("ignore");
        cmp.setIgnoreTableList(ignoreList);

        assertTrue(cmp.compareEqual(a, b));
        assertEquals(2, calledActual.size());

        assertEquals(a.getTables(), calledExpected.get(0));
        assertEquals(b.getTables(), calledActual.get(0));
        assertEquals(ignoreList, calledIgnoreTableList.get(0));
        assertEquals(cmp.schemaDiff, calledSchamaDiff.get(0));

        assertEquals(a.getViews(), calledExpected.get(1));
        assertEquals(b.getViews(), calledActual.get(1));
        assertEquals(ignoreList, calledIgnoreTableList.get(1));
        assertEquals(cmp.schemaDiff, calledSchamaDiff.get(1));
    }

    @Test
    public void twoDbSchemas_compare_callPrivilegeComparatorCorrectly() throws Exception {
        prepareEqualSchemas();

        assertEquals(1, privilegeComparatorCallCount);
        assertEquals(a.getPrivileges(), privilegeExpected);
        assertEquals(b.getPrivileges(), privilegeActual);
        assertEquals(cmp.getSchemaDifference(), privilegeSchemaDiff);
    }

    private void prepareEqualSchemas() {
        a = DbSchemas.createDbSchema("a");
        b = DbSchemas.createDbSchema("b");

        assertTrue(cmp.compareEqual(a, b));

        calledActual.clear();
        calledExpected.clear();
        calledIgnoreTableList.clear();
        calledSchamaDiff.clear();
    }


}