package org.rola.metaclerk.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseJdbcConnectorImplCompTest extends BaseJdbcConnectorImpl {

    @Before
    public void setUp() throws Exception {
        connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "");
    }

    @After
    public void tearDown() throws Exception {
        executeUpdate("drop table test");
        close();
    }

    @Test
    public void tableWithTwoStringParam_select_returnsTwoStrings() throws Exception {
        executeUpdate("create table test (param varchar(10));");
        executeUpdate("insert into test values (?);", "Vienas");
        executeUpdate("insert into test values (?);", "Du");

        executeSelectStatement("select param from test");

        assertTrue(nextRow());
        assertEquals("Vienas", getStringResult("param"));
        assertTrue(nextRow());
        assertEquals("Du", getStringResult("param"));
        assertFalse(nextRow());
    }

    @Test
    public void tableWithOneIntegerParam_select_returnsOneInteger() throws Exception {
        executeUpdate("create table test (param number(5));");
        executeUpdate("insert into test values (?);", 4);

        executeSelectStatement("select param from test");

        assertTrue(nextRow());
        assertEquals((Integer) 4, getIntResult("param"));
        assertFalse(nextRow());
    }

    @Test
    public void tableWithOneDoubleParam_select_returnsOneDouble() throws Exception {
        executeUpdate("create table test (param number(5,2));");
        executeUpdate("insert into test values (?);", 4.5d);

        executeSelectStatement("select param from test");

        assertTrue(nextRow());
        assertEquals((Double) 4.5d, getDoubleResult("param"));
        assertFalse(nextRow());
    }

    @Test
    public void tableWithOneBigDecimalParam_select_returnsOneBigDecimal() throws Exception {
        executeUpdate("create table test (param number(5,2));");
        executeUpdate("insert into test values (?);", new BigDecimal("4.5"));

        executeSelectStatement("select param from test");

        assertTrue(nextRow());
        assertEquals(new BigDecimal("4.50"), getBigDecimalResult("param"));
        assertFalse(nextRow());
    }

    @Test
    public void tableWithOneFloatParam_select_returnsOneDouble() throws Exception {
        executeUpdate("create table test (param number(5,2));");
        executeUpdate("insert into test values (?);", 4.5f);

        executeSelectStatement("select param from test");

        assertTrue(nextRow());
        assertEquals((Double)4.5, getDoubleResult("param"));
        assertFalse(nextRow());
    }

    @Test
    public void tableWithOneByteParam_select_returnsOneInteger() throws Exception {
        executeUpdate("create table test (param number(5));");
        executeUpdate("insert into test values (?);", Byte.parseByte("4"));

        executeSelectStatement("select param from test");

        assertTrue(nextRow());
        assertEquals((Integer)4, getIntResult("param"));
        assertFalse(nextRow());
    }

    @Test
    public void tableWithOneShortParam_select_returnsOneInteger() throws Exception {
        executeUpdate("create table test (param number(5));");
        executeUpdate("insert into test values (?);", Short.parseShort("2"));

        executeSelectStatement("select param from test");

        assertTrue(nextRow());
        assertEquals((Integer)2, getIntResult("param"));
        assertFalse(nextRow());
    }

    @Override
    protected void safeLoadDriver() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }
}
