package org.rola.metaclerk.model;

public class TableDescription extends DbObject {

	private ColumnList columns = new ColumnList();

	public ColumnList getColumns() {
		return columns;
	}


	public void setColumns(ColumnList columns) {
		this.columns = columns;
	}


	@Override
	public String toString() {
		return "TableDescription [getOwnerName()=" + getOwnerName() + ", getName()=" + getName() + ", columns=" + columns + "]";
	}

	@Override
	public String getObjectType() {
		return TableDescription.getStaticTypeName();
	}

	@SuppressWarnings("SameReturnValue")
	public static String getStaticTypeName() {
		return "Table";
	}

}
