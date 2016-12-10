package org.rola.metaclerk.plugin.oracle.dao.impl;

import java.sql.SQLException;

import org.rola.metaclerk.dao.api.JdbcConnector;
import org.rola.metaclerk.dao.api.JdbcResult;
import org.rola.metaclerk.dao.impl.BaseFetcher;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.ColumnList;
import org.rola.metaclerk.model.TableDescription;

class OracleTableColumnsFetcher extends BaseFetcher {
	
	public OracleTableColumnsFetcher(JdbcConnector con) {
		setConnector(con);
	}
	
	public void fillTableColumns(TableDescription tableDescription) {
		try {
			safeFillTableColumnsDecription(tableDescription);
		} catch (Exception ex) {
			throw new DAOException("Error fetching table columns", ex);
		}
	}

	private void safeFillTableColumnsDecription(TableDescription tableDescription) throws SQLException {
		ColumnList lst = new ColumnList();
		dbc.executeSelectStatement("select * from all_tab_columns c where lower(c.owner) = ? and lower(c.TABLE_NAME) = ? order by column_id",
								tableDescription.getOwnerName().toLowerCase(), tableDescription.getName().toLowerCase());
		dbc.forEachDbRow(dbc->lst.add(fetchColumnDesctiption(dbc)));
		tableDescription.setColumns(lst);
	}
	
	private ColumnDescription fetchColumnDesctiption(JdbcResult res) throws SQLException {
		ColumnDescription cd = new ColumnDescription();

		cd.setName(res.getStringResult("COLUMN_NAME"));
		cd.setType(res.getStringResult("DATA_TYPE"));
		cd.setColumnID(res.getIntResult("COLUMN_ID"));
		cd.setDataDefault(res.getStringResult("DATA_DEFAULT"));
		cd.setCharUsed(res.getStringResult("CHAR_USED"));
		cd.setDataLength(res.getIntResult("DATA_LENGTH"));
		cd.setDataPrecision(res.getIntResult("DATA_PRECISION"));
		cd.setDataScale(res.getIntResult("DATA_SCALE"));
		cd.setNullable(res.getStringResult("NULLABLE").trim().toUpperCase().equals("Y"));

		return cd;
	}
}
