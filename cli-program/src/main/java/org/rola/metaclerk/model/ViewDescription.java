package org.rola.metaclerk.model;

public class ViewDescription extends TableDescription {
    public static ViewDescription fromTableDescription(TableDescription table) {
        if (table == null)
            return null;
        ViewDescription view = new ViewDescription();
        copyAttributes(table, view);
        return view;
    }

    private static void copyAttributes(TableDescription table, ViewDescription view) {
        view.setName(table.getName());
        view.setOwnerName(table.getOwnerName());
        view.setWasFound(table.isWasFound());
        view.setColumns(table.getColumns());
    }

    @Override
    public String getObjectType() {
        return ViewDescription.getStaticTypeName();
    }

    @SuppressWarnings("SameReturnValue")
    public static String getStaticTypeName() {
        return "View";
    }
}
