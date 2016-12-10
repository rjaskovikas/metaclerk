package org.rola.metaclerk.model;

public class ViewList extends DbObjectList<ViewDescription> {

    public static ViewList formTableList(TableList tableList) {
        if (tableList == null) return null;
        ViewList viewList = new ViewList();
        tableList.forEach(table -> viewList.add(ViewDescription.fromTableDescription(table)));
        return viewList;
    }

    public TableList toTableList() {
        TableList tableList = new TableList();
        this.forEach(tableList::add);
        return tableList;
    }
}
