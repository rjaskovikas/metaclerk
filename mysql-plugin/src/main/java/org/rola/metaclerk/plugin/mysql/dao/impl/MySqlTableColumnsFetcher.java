package org.rola.metaclerk.plugin.mysql.dao.impl;

import org.rola.metaclerk.dao.api.JdbcConnector;
import org.rola.metaclerk.dao.api.JdbcResult;
import org.rola.metaclerk.dao.impl.BaseFetcher;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.ColumnList;
import org.rola.metaclerk.model.TableDescription;

import java.sql.SQLException;

class MySqlTableColumnsFetcher extends BaseFetcher {

    public MySqlTableColumnsFetcher(JdbcConnector con) {
        setConnector(con);
    }

    public void fillTableColumns(TableDescription tableDescription) {
        try {
            safeFillTableColumnsDescription(tableDescription);
        } catch (Exception ex) {
            throw new DAOException("Error fetching table columns", ex);
        }
    }

    private void safeFillTableColumnsDescription(TableDescription tableDescription) throws SQLException {
        ColumnList lst = new ColumnList();
        String query = "select * from columns where table_schema = ? and table_name = ? "
                + " order by ordinal_position";
        dbc.executeSelectStatement(query, tableDescription.getOwnerName(), tableDescription.getName());
        dbc.forEachDbRow(dbc -> lst.add(fetchColumnDescription(dbc)));
        tableDescription.setColumns(lst);
    }

    private ColumnDescription fetchColumnDescription(JdbcResult res) throws SQLException {
        ColumnDescription cd = new ColumnDescription();

        cd.setName(res.getStringResult("COLUMN_NAME"));
        cd.setType(res.getStringResult("DATA_TYPE"));
        cd.setColumnID(res.getIntResult("ORDINAL_POSITION"));
        cd.setDataDefault(res.getStringResult("COLUMN_DEFAULT"));
        cd.setCharUsed(getCharUsedType(res));
        cd.setDataLength(res.getIntResult("CHARACTER_MAXIMUM_LENGTH"));
        cd.setDataPrecision(getDataPrecision(res));
        cd.setDataScale(res.getIntResult("NUMERIC_SCALE"));
        cd.setNullable(res.getStringResult("IS_NULLABLE").trim().toUpperCase().equals("YES"));

        return cd;
    }

    private Integer getDataPrecision(JdbcResult res) throws SQLException {
        Integer dataPrecision = res.getIntResult("NUMERIC_PRECISION");
        if (dataPrecision == null)
            dataPrecision = res.getIntResult("DATETIME_PRECISION");
        return dataPrecision;
    }

    private String getCharUsedType(JdbcResult res) throws SQLException {
        Integer max_char_length = res.getIntResult("CHARACTER_MAXIMUM_LENGTH");
        Integer max_octet_length = res.getIntResult("CHARACTER_OCTET_LENGTH");
        if (max_char_length == null || max_octet_length == null || max_char_length.intValue() == 0)
            return null;
        return max_octet_length.intValue()/max_char_length.intValue() > 1 ? "C" : "B";
    }
}
