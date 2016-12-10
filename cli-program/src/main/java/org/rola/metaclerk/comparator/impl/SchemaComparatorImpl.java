package org.rola.metaclerk.comparator.impl;


import org.rola.metaclerk.comparator.api.PrivilegeListComparator;
import org.rola.metaclerk.comparator.api.SchemaComparator;
import org.rola.metaclerk.comparator.api.TableListComparator;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.diff.SchemaDifference;

import java.util.ArrayList;
import java.util.List;

public class SchemaComparatorImpl implements SchemaComparator {
    TableListComparator tableListComparator = new TableListComparatorImpl();
    PrivilegeListComparator privilegeComparator = new PrivilegeListComparatorImpl();

    private DbSchema expected;
    private DbSchema actual;
    SchemaDifference schemaDiff;
    private List<String> ignoreTableList = new ArrayList<>();

    @Override
    public boolean compareEqual(DbSchema expected, DbSchema actual) {
        init(expected, actual);
        compareTablesAndViews();
        compareObjectPrivileges();
        return isSchemasEqual();
    }

    private void init(DbSchema expected, DbSchema actual) {
        this.expected = expected;
        this.actual = actual;
        schemaDiff = new SchemaDifference();
    }

    private void compareTablesAndViews() {
        tableListComparator.compareEqual(expected.getTables(), actual.getTables(), ignoreTableList, schemaDiff);
        tableListComparator.compareEqual(expected.getViews(), actual.getViews(), ignoreTableList, schemaDiff);
    }

    private void compareObjectPrivileges() {
        privilegeComparator.compareEqual(expected.getPrivileges(), actual.getPrivileges(), schemaDiff);
    }

    @Override
    public boolean isSchemasEqual() {
        return schemaDiff != null && !schemaDiff.hasDifferences();
    }


    @Override
    public void setIgnoreTableList(List<String> ignoreTableList) {
        this.ignoreTableList = ignoreTableList;
    }

    @Override
    public SchemaDifference getSchemaDifference() {
        return schemaDiff;
    }
}
