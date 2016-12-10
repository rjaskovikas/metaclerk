package org.rola.metaclerk.model;

import org.rola.metaclerk.model.diff.ColumnDiffType;
import org.rola.metaclerk.model.diff.ColumnDifference;

public class Columns {

	static public ColumnDescription createNVARCHAR2Column(int columnId, String columnName) {
		ColumnDescription cd = new ColumnDescription();

		cd.setColumnID(columnId);
		cd.setName(columnName);
		cd.setType("NVARCHAR2");
		cd.setDataLength(5);
		cd.setDataPrecision(2);
		cd.setDataScale(6);
		cd.setDataDefault("SYSDATE"); 
		cd.setCharUsed("B");
		cd.setNullable(true);

		return cd;
	}

	@SuppressWarnings("SameParameterValue")
    static public ColumnDescription createNotTrimmedNVARCHAR2Column(int columnId, String columnName) {
		ColumnDescription cd = new ColumnDescription();

		cd.setColumnID(columnId);
		cd.setName(columnName);
		cd.setType("NVARCHAR2");
		cd.setDataLength(5);
		cd.setDataPrecision(2);
		cd.setDataScale(6);
		cd.setDataDefault("SYSDATE\n\r"); // oracle has defaults values with new line symbols
		cd.setCharUsed("B");
		cd.setNullable(true);

		return cd;
	}

	public static ColumnDescription createNUMBERColumn(int columnId, String columnName) {
		ColumnDescription cd = new ColumnDescription();
		cd.setColumnID(columnId);
		cd.setName(columnName);
		cd.setType("NUMBER");
		cd.setDataLength(5);
		cd.setDataPrecision(2);
		cd.setDataScale(6);
		cd.setDataDefault("15");
		cd.setCharUsed("B");
		cd.setNullable(false);
		return cd;
	}

    public static ColumnDifference createColumnPrecisionDifference() {
		ColumnDifference colDiff = new ColumnDifference();
		colDiff.addColumnDiff(ColumnDiffType.PRECISION);
		colDiff.setExpectedCol(createNUMBERColumn(1, "expected"));
		colDiff.setActualCol(createNUMBERColumn(1, "actual"));
		return colDiff;
    }

	public static ColumnDifference createColumnPrecisionDifferenceWithWarning(String warningMessage) {
		ColumnDifference dif = createColumnPrecisionDifference();
		dif.addWarning(warningMessage);
		return dif;
	}
}
