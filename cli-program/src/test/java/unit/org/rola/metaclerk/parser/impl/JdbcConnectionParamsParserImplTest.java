package org.rola.metaclerk.parser.impl;

import org.junit.Test;
import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.utils.ArrayIterator;

import static org.junit.Assert.*;

public class JdbcConnectionParamsParserImplTest extends JdbcConnectionParamsParserImpl {
    private int notParserParamsCount;

    @Test
    public void allMandatoryParams_checkMandatoryParams_returnsTrue() throws Exception {
        parse(new String[] {JDBC_CONNECTION_PARAM, "jdbc_c",
                DB_USER_PARAM, "user",
                DB_PASSWORD_PARAM, "pswd",
                SCHEMA_NAME_PARAM, "schema"});

        checkMandatoryParams();
    }

    @Test
    public void allMandatoryParams_parse_readsAllParams() throws Exception {
        parse(new String[] {JDBC_CONNECTION_PARAM, "jdbc_c",
                DB_USER_PARAM, "user",
                DB_PASSWORD_PARAM, "pswd",
                SCHEMA_NAME_PARAM, "schema",
                DATABASE_TYPE_PARAM, "dbType"});

        assertEquals("jdbc_c", getJdbcConnectionStr());
        assertEquals("user", getDbUser());
        assertEquals("pswd", getDbPassword());
        assertEquals("schema", getSchemaName());
        assertEquals("dbType", getDbType());
        assertEquals(0, notParserParamsCount);
    }

    @Test
    public void allMandatoryParamsAndOneForeign_parse_readsAllParamsAndIgnoresForeign() throws Exception {
        parse(new String[] {JDBC_CONNECTION_PARAM, "jdbc_c",
                DB_USER_PARAM, "user",
                DB_PASSWORD_PARAM, "pswd",
                SCHEMA_NAME_PARAM, "schema",
                "-foreign"});

        assertEquals("jdbc_c", getJdbcConnectionStr());
        assertEquals("user", getDbUser());
        assertEquals("pswd", getDbPassword());
        assertEquals("schema", getSchemaName());
        assertEquals(1, notParserParamsCount);
    }

    @Test
    public void parserObjec_getParamsDescriptionString_returnsTwoParams() throws Exception {
        assertTrue(getParamsDescriptionString().contains(JDBC_CONNECTION_PARAM));
        assertTrue(getParamsDescriptionString().contains(DB_USER_PARAM));
        assertTrue(getParamsDescriptionString().contains(DB_PASSWORD_PARAM));
        assertTrue(getParamsDescriptionString().contains(SCHEMA_NAME_PARAM));
        assertTrue(getParamsDescriptionString().contains(DATABASE_TYPE_PARAM));
    }

    @Test
    public void parserObjec_getParamsUsageString_returnsTwolines() throws Exception {
        assertTrue(getParamsUsageString().contains(JDBC_CONNECTION_PARAM));
        assertTrue(getParamsUsageString().contains(DB_USER_PARAM));
        assertTrue(getParamsUsageString().contains(DB_PASSWORD_PARAM));
        assertTrue(getParamsUsageString().contains(SCHEMA_NAME_PARAM));
        assertTrue(getParamsUsageString().contains(DATABASE_TYPE_PARAM));
    }


    @Test(expected = BadParamException.class)
    public void allParamsExceptConnectioString_parse_validationThrowsError() throws Exception {
        parse(new String[] {DB_USER_PARAM, "user",
                            DB_PASSWORD_PARAM, "pswd",
                            SCHEMA_NAME_PARAM, "schema"});

        checkMandatoryParams();
    }

    @Test(expected = BadParamException.class)
    public void allParamsExceptDbUser_parse_validationThrowsError() throws Exception {
        parse(new String[] {JDBC_CONNECTION_PARAM, "jdbc_c",
                DB_PASSWORD_PARAM, "pswd",
                SCHEMA_NAME_PARAM, "schema"});

        checkMandatoryParams();
    }

    @Test(expected = BadParamException.class)
    public void allParamsExceptDbPassword_parse_validationThrowsError() throws Exception {
        parse(new String[] {JDBC_CONNECTION_PARAM, "jdbc_c",
                DB_USER_PARAM, "user",
                SCHEMA_NAME_PARAM, "schema"});

        checkMandatoryParams();
    }

    @Test(expected = BadParamException.class)
    public void allParamsExceptSchema_parse_validationThrowsError() throws Exception {
        parse(new String[] {JDBC_CONNECTION_PARAM, "jdbc_c",
                DB_USER_PARAM, "user",
                DB_PASSWORD_PARAM, "pswd"});

        checkMandatoryParams();
    }

    @Test
    public void jdbcConnection_parse_printsCommandUsage() throws Exception {
        parse(new String[] {JDBC_CONNECTION_PARAM, "jdbc_str"});

        assertEquals("jdbc_str", getJdbcConnectionStr());
        assertNull(getDbUser());
        assertNull(getDbPassword());
        assertNull(getSchemaName());
    }

    @Test
    public void dbUser_parse_printsCommandUsage() throws Exception {
        parse(new String[] {DB_USER_PARAM, "user"});

        assertEquals("user", getDbUser());
        assertNull(getJdbcConnectionStr());
        assertNull(getDbPassword());
        assertNull(getSchemaName());
    }

    @Test
    public void dbPassword_parse_printsCommandUsage() throws Exception {
        parse(new String[] {DB_PASSWORD_PARAM, "pwd"});

        assertEquals("pwd", getDbPassword());
        assertNull(getJdbcConnectionStr());
        assertNull(getDbUser());
        assertNull(getSchemaName());
    }

    @Test
    public void schemaName_parse_printsCommandUsage() throws Exception {
        parse(new String[] {SCHEMA_NAME_PARAM, "schema"});

        assertEquals("schema", getSchemaName());
        assertNull(getJdbcConnectionStr());
        assertNull(getDbUser());
        assertNull(getDbPassword());
    }

    @Test(expected = BadParamException.class)
    public void UserParamWithNoValue_parse_throwsException() throws Exception {
        parse(new String[]{DB_USER_PARAM});
    }

    @Test(expected = BadParamException.class)
    public void passwordParamWithNoValue_parse_throwsException() throws Exception {
        parse(new String[]{DB_PASSWORD_PARAM});
    }

    @Test(expected = BadParamException.class)
    public void jdbcConnectionParamWithNoValue_parse_throwsException() throws Exception {
        parse(new String[]{JDBC_CONNECTION_PARAM});
    }

    @Test(expected = BadParamException.class)
    public void schemaParamWithNoValue_parse_throwsException() throws Exception {
        parse(new String[]{SCHEMA_NAME_PARAM});
    }

    @Test(expected = BadParamException.class)
    public void dbTypeParamWithNoValue_parse_throwsException() throws Exception {
        parse(new String[]{DATABASE_TYPE_PARAM});
    }

    private void parse(String[] params) {
        setup(new ArrayIterator<>(params));
        while (paramIt.hasNext()) {
            paramIt.next();
            if (!parseParam())
                notParserParamsCount++;
        }
    }
}