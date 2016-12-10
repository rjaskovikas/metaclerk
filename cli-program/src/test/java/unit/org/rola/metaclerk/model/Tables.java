package org.rola.metaclerk.model;

import org.rola.metaclerk.model.diff.TableDiffType;
import org.rola.metaclerk.model.diff.TableDifference;

public class Tables {

	private static final String DEFAULT_TABLE_NAME = "TableName";
	private static final String DEFAULT_TABLE_OWNER = "TableOwner";

	private static TableDescription createTableWithNoColumns(String tableName, String tableOwner) {
		TableDescription t = new TableDescription();
		t.setName(tableName);
		t.setOwnerName(tableOwner);
		return t;
	}

	static public TableDescription createTableWithNoColumns() {
		return createTableWithNoColumns(DEFAULT_TABLE_NAME, DEFAULT_TABLE_OWNER);
	}

	static public TableDescription createTableWithColumns(String tableName, String tableOwner) {
		TableDescription table = createTableWithNoColumns(tableName, tableOwner);
		table.getColumns().add(Columns.createNotTrimmedNVARCHAR2Column(1, "Column1"));
		table.getColumns().add(Columns.createNUMBERColumn(2, "Column2"));
		return table;
	}

	static public TableDescription createTableWithColumns() {
		return createTableWithColumns(DEFAULT_TABLE_NAME, DEFAULT_TABLE_OWNER);
	}

	public static TableList createTables(String schema, String ... tableNames) {
		TableList lst = new TableList();
		for (String tableName : tableNames) {
			TableDescription table = createTableWithColumns(tableName, schema);
			lst.add(table);
		}
		return lst;
	}

	public static TableDifference createTableNameDifference() {
		TableDifference tblDiff = new TableDifference();
		tblDiff.setExpectedTable(Tables.createTableWithColumns("expected", "schema"));
		tblDiff.setActualTable(Tables.createTableWithColumns("actual", "schema"));
		tblDiff.addTableDifference(TableDiffType.NAME);
		return tblDiff;
	}
}
