package org.rola.metaclerk.comparator.impl;

import org.junit.Test;
import org.rola.metaclerk.comparator.api.PrivilegeListComparator;
import org.rola.metaclerk.model.PrivilegeList;
import org.rola.metaclerk.model.diff.PrivilegeDiffType;
import org.rola.metaclerk.model.diff.SchemaDifference;
import org.rola.metaclerk.xml.impl.Privileges;

import static org.junit.Assert.*;

public class PrivilegeListComparatorImplTest {
    private final PrivilegeListComparator comp = new PrivilegeListComparatorImpl();
    private final SchemaDifference schemaDiff = new SchemaDifference();
    private PrivilegeList expected;
    private PrivilegeList actual;

    @Test(expected = NullPointerException.class)
    public void nullObjects_compare_throwsNullPointerException() throws Exception {
        comp.compareEqual(null, null, null);
    }

    @Test
    public void samePrivilegeList_compare_returnsTrue() throws Exception {
        prepareDiffLists();
        assertTrue(comp.compareEqual(expected, actual, schemaDiff));
        assertFalse(schemaDiff.hasPrivilegeDifferences());
    }

    @Test
    public void expectedHasNewPrivilege_compare_returnsFalseAndDiffListHasExpectedNotFoundDifference() throws Exception {
        prepareDiffLists();
        expected.add(Privileges.createPrivilage("additional", "test", "SELECT", "NO"));

        assertFalse(comp.compareEqual(expected, actual, schemaDiff));
        assertEquals(1, schemaDiff.getPrivilegeDifferences().size());
        assertEquals(PrivilegeDiffType.EXPECTED_NOT_FOUNT, schemaDiff.getPrivilegeDifferences().get(0).getDifference());
    }

    @Test
    public void actualHasNewPrivilege_compare_returnsFalseAndDiffListHasNewActualDifference() throws Exception {
        prepareDiffLists();
        actual.add(Privileges.createPrivilage("additional", "test", "SELECT", "NO"));

        assertFalse(comp.compareEqual(expected, actual, schemaDiff));
        assertEquals(1, schemaDiff.getPrivilegeDifferences().size());
        assertEquals(PrivilegeDiffType.ACTUAL_NEW, schemaDiff.getPrivilegeDifferences().get(0).getDifference());
    }

    @Test
    public void samePrivilegeWithDiffGrantable_comapre_returnsFalseAndDiffListHasGrantDifference() throws Exception {
        prepareDiffLists();
        actual.get(0).setGrantable("DiffGrantable");

        assertFalse(comp.compareEqual(expected, actual, schemaDiff));
        assertEquals(1, schemaDiff.getPrivilegeDifferences().size());
        assertEquals(PrivilegeDiffType.GRANT, schemaDiff.getPrivilegeDifferences().get(0).getDifference());
    }

    private void prepareDiffLists() {
        actual = Privileges.createPrivilegeList();
        expected = Privileges.createPrivilegeList();
    }
}