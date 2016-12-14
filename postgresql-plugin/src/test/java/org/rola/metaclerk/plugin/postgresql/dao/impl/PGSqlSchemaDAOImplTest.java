package org.rola.metaclerk.plugin.postgresql.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.dao.api.JdbcConnector;
import org.rola.metaclerk.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

public class PGSqlSchemaDAOImplTest {

    private JdbcConnector dbc;
    private PGSqlSchemaDAOImpl dao;
    private boolean callSuperSelectInformationSchema;
    private List<String> tablesToDelete = new ArrayList<>();
    private boolean reinitDAOInTearDown;

    @Before
    public void setUp() throws Exception {
        dbc = new PGSqlJdbcConnectorImpl() {
            @Override
            protected void safeLoadDriver() throws ClassNotFoundException {
                Class.forName("org.h2.Driver");
            }
        };
        dao = new PGSqlSchemaDAOImpl(dbc) {
            @Override
            void selectInformationSchema() {
                // select information_schema only for schema selection test. All other test will use
                // fake schema, with fake system tables
                if (callSuperSelectInformationSchema)
                    super.selectInformationSchema();
            }
        };

    }

    @After
    public void tearDown() throws Exception {
        if (reinitDAOInTearDown)
            initDao();

        for (String tb : tablesToDelete)
            dbc.executeUpdate("drop table " + tb + ";");

        dao.closeDAO();
    }

    private void initDao() {
        dao.initDAO("jdbc:h2:mem:postgresqlTest;DB_CLOSE_DELAY=-1;MODE=PostgreSQL", "", "");
    }

    @Test
    public void initializedDaoObject_closeDAO_closesJDBCConnection() throws Exception {
        initDao();
        createFakeTablesTableAndFillWithData();
        dao.getSchemaTableList("PostgresqlSchema"); // should not throw an exception because jdbc connection is open
        dao.closeDAO();
        try {
            dao.getSchemaTableList("PostgresqlSchema"); // should throw an exception because jdbc connection is closed
        } catch (Exception ex) {
            reinitDAOInTearDown = true;
            return;
        }
        assertFalse(true); //error if this code is reached
    }


    @Test
    public void normalDbInitializationSequence_initDab_SelectsInformationSchema() throws Exception {
        callSuperSelectInformationSchema = true;
        JdbcConnector dbcMock = mock(JdbcConnector.class);
        dao.setDbc(dbcMock);
        initDao();
        verify(dbcMock, times(1)).executeUpdate("set schema 'information_schema'");
    }

    @Test(expected = SQLException.class)
    public void fakeDbInitializationSequence_initDao_emptySchemaSelected() throws Exception {
        callSuperSelectInformationSchema = false;
        initDao();
        dbc.executeSelectStatement("select * from table_privileges"); // must throw SQLException because table doesn't exist
    }

    @Test
    public void schemaTableList_getSchemaTableList_returnsTwoTable() throws Exception {
        initDao();
        createFakeTablesTableAndFillWithData();
        TableList lst = dao.getSchemaTableList("PostgresqlSchema");
        checkTableListResult(lst);
    }

    private void checkTableListResult(TableList lst) {
        assertEquals(2, lst.size());
        assertEquals("test1_table", lst.get(0).getName());
        assertEquals("PostgresqlSchema", lst.get(0).getOwnerName());
        assertEquals("test2_table", lst.get(1).getName());
        assertEquals("PostgresqlSchema", lst.get(1).getOwnerName());
    }

    @Test
    public void schemaTableListWithColumns_fillTableColumnDescription_returnTwoColumns() throws Exception {
        initDao();
        createFakeTablesTableAndFillWithData();
        createFakeColumnsTableAndFillWithData();
        TableDescription tbl = new TableDescription();
        tbl.setName("test1_table");
        tbl.setOwnerName("PostgresqlSchema");

        dao.fillTableColumnDescription(tbl);

        checkTableColumnsResult(tbl);
    }

    private void checkTableColumnsResult(TableDescription tbl) {
        assertEquals(2, tbl.getColumns().size());
        ColumnDescription cl = tbl.getColumns().get(0);
        assertEquals("column1", cl.getName());
        assertNull(cl.getOwnerName());
        assertEquals(new Integer(1), cl.getColumnID());
        assertEquals("varchar", cl.getType());
        assertEquals(new Integer(20), cl.getDataLength());
        assertNull(cl.getDataPrecision());
        assertNull(cl.getDataScale());
        assertEquals("C", cl.getCharUsed());
    }

    @Test
    public void schemaViewList_getSchemaViewList_returnsTwoViews() throws Exception {
        initDao();
        createFakeTablesTableAndFillWithData();
        ViewList lst = dao.getSchemaViewList("PostgresqlSchema");
        checkViewListResult(lst);
    }

    private void checkViewListResult(ViewList lst) {
        assertEquals(2, lst.size());
        assertEquals("test1_view", lst.get(0).getName());
        assertEquals("PostgresqlSchema", lst.get(0).getOwnerName());
        assertEquals("test2_view", lst.get(1).getName());
        assertEquals("PostgresqlSchema", lst.get(1).getOwnerName());
    }

    @Test
    public void schemaViewListWithColumns_fillViewColumnDescription_returnTwoColumns() throws Exception {
        initDao();
        createFakeColumnsTableAndFillWithData();
        ViewDescription vw = new ViewDescription();
        vw.setName("test1_view");
        vw.setOwnerName("PostgresqlSchema");

        dao.fillTableColumnDescription(vw);

        checkViewColumnsResult(vw);
    }

    private void checkViewColumnsResult(ViewDescription vw) {
        assertEquals(2, vw.getColumns().size());
        ColumnDescription cl = vw.getColumns().get(0);
        assertEquals("column1", cl.getName());
        assertNull(cl.getOwnerName());
        assertEquals(new Integer(1), cl.getColumnID());
        assertEquals("varchar", cl.getType());
        assertEquals(new Integer(20), cl.getDataLength());
        assertNull(cl.getDataPrecision());
        assertNull(cl.getDataScale());
        assertEquals("C", cl.getCharUsed());
    }

    @Test
    public void schemaPrivileges_getDbUserPrivilegeList_returnsTwoPrivileges() throws Exception {
        initDao();
        createFakePrivilegesTableAndFillWithData();
        PrivilegeList lst = dao.getDbUserPrivilegeList("PostgresqlSchema");
        checkPrivilegesResult(lst);
    }

    private void checkPrivilegesResult(PrivilegeList lst) {
        assertEquals(2, lst.size());
        assertEquals("test1_table", lst.get(0).getName());
        assertEquals("PostgresqlSchema", lst.get(0).getOwnerName());
        assertEquals("YES", lst.get(0).getGrantable());
        assertEquals("SELECT", lst.get(0).getPrivilege());

        assertEquals("test1_table", lst.get(1).getName());
        assertEquals("PostgresqlSchema", lst.get(1).getOwnerName());
        assertEquals("NO", lst.get(1).getGrantable());
        assertEquals("UPDATE", lst.get(1).getPrivilege());
    }

    @Test
    public void schemaMetadata_getAllSchemaObjects_returnsFilledSchemaObject() throws Exception {
        initDao();
        createFakeTablesTableAndFillWithData();
        createFakeColumnsTableAndFillWithData();
        createFakePrivilegesTableAndFillWithData();

        DbSchema schema = dao.getAllSchemaObjects("PostgresqlSchema");

        checkTableListResult(schema.getTables());
        checkTableColumnsResult(schema.getTables().get(0));
        checkViewListResult(schema.getViews());
        checkViewColumnsResult(schema.getViews().get(0));
        checkPrivilegesResult(schema.getPrivileges());
    }

    private void createFakePrivilegesTableAndFillWithData() throws SQLException {
        createFakePrivilegesTable();
        fillPrivilegesTableWithData();
    }

    private void fillPrivilegesTableWithData() throws SQLException {
        String
                query = " insert into table_privileges (grantee, table_schema, table_name, privilege_type, is_grantable)" +
                " values ('PostgresqlSchema', 'PostgresqlSchema', 'test1_table', 'SELECT', 'YES')";
        dbc.executeUpdate(query);
        query = " insert into table_privileges (grantee, table_schema, table_name, privilege_type, is_grantable)" +
                " values ('PostgresqlSchema', 'PostgresqlSchema', 'test1_table', 'UPDATE', 'NO')";
        dbc.executeUpdate(query);
        // creating one record that should not be selected
        query = " insert into table_privileges (grantee, table_schema, table_name, privilege_type, is_grantable)" +
                " values ('PostgresqlSchema-no', 'PostgresqlSchema', 'test1_table', 'UPDATE', 'NO')";
        dbc.executeUpdate(query);
    }

    private void createFakePrivilegesTable() throws SQLException {
        tablesToDelete.add("table_privileges");
        String
                query = "" +
                "CREATE TABLE `TABLE_PRIVILEGES` (\n" +
                "  `GRANTEE` varchar(81) NOT NULL DEFAULT '',\n" +
                "  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',\n" +
                "  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',\n" +
                "  `PRIVILEGE_TYPE` varchar(64) NOT NULL DEFAULT '',\n" +
                "  `IS_GRANTABLE` varchar(3) NOT NULL DEFAULT ''\n" +
                ")";
        dbc.executeUpdate(query);
    }

    private void createFakeTablesTableAndFillWithData() throws SQLException {
        createTablesTable();
        fillTablesTableWithData("table", "BASE TABLE");
        fillTablesTableWithData("view", "VIEW");
    }

    private void fillTablesTableWithData(String object_type, String table_type) throws SQLException {
        String query = "insert into tables (table_name, table_schema, table_type) " +
                "values ('test1_" + object_type + "', 'PostgresqlSchema', '" + table_type + "')";
        dbc.executeUpdate(query);
        query = "insert into tables (table_name, table_schema, table_type) " +
                "values ('test2_" + object_type + "', 'PostgresqlSchema','" + table_type + "')";
        dbc.executeUpdate(query);

        // creating one record that should not be selected
        query = "insert into tables (table_name, table_schema, table_type) " +
                "values ('test3_" + object_type + "', 'PostgresqlSchema','" + table_type + "-no" + "')";
        dbc.executeUpdate(query);
    }

    private void createTablesTable() throws SQLException {
        tablesToDelete.add("tables");
        String query = "create table tables " +
                "(" +
                "table_name varchar(20)," +
                "table_schema varchar(20)," +
                "table_type varchar(20)" +
                ")";
        dbc.executeUpdate(query);
    }

    private void createFakeColumnsTableAndFillWithData() throws SQLException {
        createColumnsTable();
        fillColumnsTableWithData("table");
        fillColumnsTableWithData("view");
    }

    private void fillColumnsTableWithData(String object_type) throws SQLException {
        String
                query = "" +
                "insert into columns (table_schema, table_name, column_name, ordinal_position, column_default, " +
                "is_nullable, data_type, character_maximum_length, character_octet_length, numeric_precision," +
                "numeric_scale, datetime_precision) " +
                "values ('PostgresqlSchema', 'test1_" + object_type + "', 'column1', 1, 'def1'," +
                "'YES', 'varchar', 20, 60, null, " +
                "null, null)";
        dbc.executeUpdate(query);
        query = "" +
                "insert into columns (table_schema, table_name, column_name, ordinal_position, column_default, " +
                "is_nullable, data_type, character_maximum_length, character_octet_length, numeric_precision," +
                "numeric_scale, datetime_precision) " +
                "values ('PostgresqlSchema', 'test1_" + object_type + "', 'column2', 2, 'def2'," +
                "'NO', 'DECIMAL', null, null, 7, " +
                "2, null)";
        dbc.executeUpdate(query);
        // creating record that should not be selected in tests
        query = "" +
                "insert into columns (table_schema, table_name, column_name, ordinal_position, column_default, " +
                "is_nullable, data_type, character_maximum_length, character_octet_length, numeric_precision," +
                "numeric_scale, datetime_precision) " +
                "values ('PostgresqlSchema', 'test1-no_" + object_type + "', 'column1', 1, 'def'," +
                "'NO', 'DECIMAL', null, null, 7, " +
                "2, null)";
        dbc.executeUpdate(query);
    }

    private void createColumnsTable() throws SQLException {
        tablesToDelete.add("columns");
        String query = "CREATE TABLE `COLUMNS` (\n" +
                "  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',\n" +
                "  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',\n" +
                "  `COLUMN_NAME` varchar(64) NOT NULL DEFAULT '',\n" +
                "  `ORDINAL_POSITION` bigint(21) unsigned NOT NULL DEFAULT '0',\n" +
                "  `COLUMN_DEFAULT` longtext,\n" +
                "  `IS_NULLABLE` varchar(3) NOT NULL DEFAULT '',\n" +
                "  `DATA_TYPE` varchar(64) NOT NULL DEFAULT '',\n" +
                "  `CHARACTER_MAXIMUM_LENGTH` bigint(21) unsigned DEFAULT NULL,\n" +
                "  `CHARACTER_OCTET_LENGTH` bigint(21) unsigned DEFAULT NULL,\n" +
                "  `NUMERIC_PRECISION` bigint(21) unsigned DEFAULT NULL,\n" +
                "  `NUMERIC_SCALE` bigint(21) unsigned DEFAULT NULL,\n" +
                "  `DATETIME_PRECISION` bigint(21) unsigned DEFAULT NULL\n" +
                ")";
        dbc.executeUpdate(query);
    }
}