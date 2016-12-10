package org.rola.metaclerk.plugin.oracle.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.dao.impl.BaseJdbcConnectorImpl;
import org.rola.metaclerk.exception.dao.DAOException;
import org.rola.metaclerk.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class OracleSchemaDAOImplCompTest extends OracleSchemaDAOImpl {
    private final List<String> tablesToDelete = new ArrayList<>();
    private boolean reinitDAOInTearDown;

    public OracleSchemaDAOImplCompTest() {
        super(new H2JdbcConnector());
    }

    static class H2JdbcConnector extends BaseJdbcConnectorImpl {
        @Override
        protected void safeLoadDriver() throws ClassNotFoundException {
            Class.forName("org.h2.Driver");
        }
    }

    @Before
    public void setUp() throws Exception {
        initDAO();
    }

    private void initDAO(){
        this.initDAO("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "");
    }

    @After
    public void tearDown() throws Exception {
        if (reinitDAOInTearDown)
            initDAO();

        for (String tb : tablesToDelete)
            this.dbc.executeUpdate("drop table " + tb + ";");

        this.closeDAO();
    }

    @Test
    public void initializedDaoObject_closeDAO_closesJDBCConnection() throws Exception {

        createAllObjectsTableAndInsertData(false, "VIEW");
        getSchemaViewList("test"); // should not throw an exception because jdbc connection is open
        this.closeDAO();
        try {
            getSchemaViewList("test"); // should throw an exception because jdbc connection is closed
        } catch (Exception ex) {
            reinitDAOInTearDown = true;
            return;
        }
        assertFalse(true); //error if this code is reached
    }

    @Test
    public void schemaTableList_getSchemaTableList_returnsTwoTables() throws Exception {
        createAllObjectsTableAndInsertData(false, "TABLE");
        TableList lst = getSchemaTableList("test");
        assertEquals(2, lst.size());
    }

    @Test
    public void schemaViewList_getSchemaViewList_returnsTwoViews() throws Exception {
        createAllObjectsTableAndInsertData(false, "VIEW");
        ViewList lst = getSchemaViewList("test");
        assertEquals(2, lst.size());
    }

    @Test (expected = DAOException.class)
    public void schemaTableListWithBadColumnName_getSchemaTableList_throwsDaoException() throws Exception {
        createAllObjectsTableAndInsertData(true, "TABLE");
        getSchemaTableList("test");
    }

    @Test (expected = DAOException.class)
    public void schemaViewListWithBadColumnName_getSchemaViewList_throwsDAOException() throws Exception {
        createAllObjectsTableAndInsertData(true, "VIEW");
        getSchemaViewList("test");
    }

    @Test
    public void schemaTableList_getAllSchemaObject_returnsSchemaWithTwoTableAndTwoColumns() throws Exception {
        createAllObjectsTableAndInsertData(false, "TABLE");
        createAllTabColumnsTableAndInsertData(false);
        createPrivilegeTableAndFillWithData(false);

        DbSchema sc  = getAllSchemaObjects("test");
        assertNotNull(sc);
        assertEquals(2, sc.getTables().size());
        assertEquals(0, sc.getViews().size());
        assertEquals(2, sc.getTables().get(0).getColumns().size());
        assertEquals(2, sc.getPrivileges().size());
//        System.out.println(sc.toString());
    }

    @Test
    public void schemaViewList_getAllSchemaObject_returnsSchemaWithTwoViewsAndTwoColumns() throws Exception {
        createAllObjectsTableAndInsertData(false, "VIEW");
        createAllTabColumnsTableAndInsertData(false);
        createPrivilegeTableAndFillWithData(false);

        DbSchema sc  = getAllSchemaObjects("test");
        assertNotNull(sc);
        assertEquals(0, sc.getTables().size());
        assertEquals(2, sc.getViews().size());
        assertEquals(2, sc.getViews().get(0).getColumns().size());
        assertEquals(2, sc.getPrivileges().size());
//        System.out.println(sc.toString());
    }

    @Test (expected = DAOException.class)
    public void schemaTableListWithError_getAllSchemaObject_throwsDaoException() throws Exception {
        createAllObjectsTableAndInsertData(true, "TABLE");
        createAllTabColumnsTableAndInsertData(true);

        getAllSchemaObjects("test");
    }

    @Test
    public void schemaTable_fillTableColumnDescription_returnsTwoColumns() throws Exception {
        TableDescription table = new TableDescription();
        table.setOwnerName("test");
        table.setName("test1");

        createAllTabColumnsTableAndInsertData(false);

        super.fillTableColumnDescription(table);
        assertEquals(2, table.getColumns().size());
    }

    @Test
    public void schemaViewList_fillViewColumnDescription_returnsTwoColumns() throws Exception {
        ViewDescription view = new ViewDescription();
        view.setOwnerName("test");
        view.setName("test1");

        createAllTabColumnsTableAndInsertData(false);

        fillViewColumnDescription(view);
        assertEquals(2, view.getColumns().size());
    }

    @Test (expected = DAOException.class)
    public void schemaTableWithSqlError_fillTableColumnDescription_throwsDaoException() throws Exception {
        TableDescription table = new TableDescription();
        table.setOwnerName("test");
        table.setName("test1");

        createAllTabColumnsTableAndInsertData(true);

        super.fillTableColumnDescription(table);
    }

    @Test (expected = DAOException.class)
    public void schemaViewListWithError_fillViewColumnDescription_throwsDaoException() throws Exception {
        ViewDescription view = new ViewDescription();
        view.setOwnerName("test");
        view.setName("test1");

        createAllTabColumnsTableAndInsertData(true);

        fillViewColumnDescription(view);
    }

    @Test
    public void schemaPrivilegeList_getSchemaPrivilegeList_returnsTwoPrivileges() throws Exception {
        createPrivilegeTableAndFillWithData(false);
        PrivilegeList lst = getDbUserPrivilegeList("TEST"); // "TEST" is used to check that schema name case doesn't matter

//        System.out.println(StreamUtils.getSystemErrBuffer());
        assertEquals(2, lst.size());
        checkDbPrivilege(lst, 0, "grantor", "test1", "select", "YES");
        checkDbPrivilege(lst, 1, "grantor", "test2", "update", "NO");
    }


    @Test (expected = DAOException.class)
    public void schemaPrivilegeListWithError_getSchemaPrivilegeList_throwsDaoException() throws Exception {
        createPrivilegeTableAndFillWithData(true);
        getDbUserPrivilegeList("test");
    }

    private void checkDbPrivilege(PrivilegeList lst, int idx, String owner, String name, String privilege, String grant) {
        assertEquals(owner, lst.get(idx).getOwnerName());
        assertEquals(name, lst.get(idx).getName());
        assertEquals(privilege, lst.get(idx).getPrivilege());
        assertEquals(grant, lst.get(idx).getGrantable());
    }

    private void createAllTabColumnsTableAndInsertData(boolean withError) throws SQLException {
        this.tablesToDelete.add("all_tab_columns");
        this.dbc.executeUpdate("create Table all_tab_columns (TABLE_NAME varchar(20) " +
                ", OWNER varchar(20),  COLUMN_NAME varchar(20)" +
                ", DATA_TYPE varchar(20), COLUMN_ID NUMBER(10), DATA_DEFAULT varchar(20)" +
                ", CHAR_USED varchar(20), DATA_LENGTH NUMBER(5), DATA_PRECISION NUMBER(5) " +
                ", DATA_SCALE NUMBER(5), " + getAllTabColumnsTableColumnName(withError) + " CHAR(1));");

        this.dbc.executeUpdate("insert into all_tab_columns " +
                "(COLUMN_ID, TABLE_NAME, OWNER, COLUMN_NAME, DATA_TYPE" +
                ", DATA_DEFAULT, CHAR_USED,DATA_LENGTH, DATA_PRECISION, DATA_SCALE," + getAllTabColumnsTableColumnName(withError) + ")" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "1", "test1", "test", "colName1", "VARCHAR2", "", "B", "20", "2", "7", "Y");

        this.dbc.executeUpdate("insert into all_tab_columns " +
               "(COLUMN_ID, TABLE_NAME, OWNER, COLUMN_NAME, DATA_TYPE" +
                ", DATA_DEFAULT, CHAR_USED,DATA_LENGTH, DATA_PRECISION, DATA_SCALE," + getAllTabColumnsTableColumnName(withError) + ")" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                "2", "test1", "test", "colName2", "NUMBER", "SYSDATE", "B", "20", "2", "7", "");
    }

    private String getAllTabColumnsTableColumnName(boolean withError) {
        return withError ? "NULLABLE_ERR" : "NULLABLE";
    }

    private String getAllObjectsTableColumnName(boolean withError) {
        return withError ? "OBJECT_TYPE_ERR" : "OBJECT_TYPE";
    }

    private void createAllObjectsTableAndInsertData(boolean withError, String objectType) throws SQLException {
        this.tablesToDelete.add("all_objects");
        this.dbc.executeUpdate("create table all_objects (OBJECT_NAME varchar(20), OWNER varchar(20), "
                + getAllObjectsTableColumnName(withError) +" varchar(20));");
        this.dbc.executeUpdate("insert into all_objects values (?, ?, ?);", "test1", "test", objectType);
        this.dbc.executeUpdate("insert into all_objects values (?, ?, ?);", "test2", "test", objectType);
    }

    private void createPrivilegeTableAndFillWithData(boolean withError) throws SQLException {
        this.tablesToDelete.add("user_tab_privs");
        this.dbc.executeUpdate("create table user_tab_privs " +
                                    "(TABLE_NAME varchar(20), " +
                                    "GRANTOR varchar(20)," +
                                    "GRANTEE varchar(20)," +
                                    "privilege varchar(20)," +
                                    "grantable" + (withError?"aaa":"") + " varchar(20));");

        this.dbc.executeUpdate("insert into user_tab_privs " +
                        "(TABLE_NAME, GRANTOR, GRANTEE, privilege, grantable" + (withError?"aaa":"") +  ")" +
                        " values (?, ?, ?, ?, ?);", "test1", "grantor", "test", "select", "YES");

        this.dbc.executeUpdate("insert into user_tab_privs " +
                "(TABLE_NAME, GRANTOR, GRANTEE, privilege, grantable" + (withError?"aaa":"") +  ")" +
                " values (?, ?, ?, ?, ?);", "test2", "grantor", "TEST", "update", "NO");
    }
}