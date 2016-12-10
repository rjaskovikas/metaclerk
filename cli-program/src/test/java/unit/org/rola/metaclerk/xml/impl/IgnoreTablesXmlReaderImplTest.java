package org.rola.metaclerk.xml.impl;

import org.junit.Test;
import org.rola.metaclerk.exception.xml.UnknownAttributeException;
import org.rola.metaclerk.exception.xml.UnsuportedElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class IgnoreTablesXmlReaderImplTest {
    private final IgnoreTablesXmlReaderImpl reader = new IgnoreTablesXmlReaderImpl();
    static protected XMLInputFactory factory = XMLInputFactory.newInstance();
    private InputStream inputStream;

    @Test(expected=XMLStreamException.class)
    public void nullReader_deserialize_throwXMLStreamException() throws Exception {
        reader.deserializeTableList(null);
    }

    @Test
    public void emptyTablesList_deserialize_returnsEmptyList() throws Exception {
        initEventReader("<?xml version=\"1.0\"?><tables2ignore/>");
        List<String> lst = reader.deserializeTableList(inputStream);
        assertNotNull(lst);
        assertEquals(0, lst.size());
    }

    @Test
    public void tableListWithOneTable_deserialize_returnsListWithOneTableName() throws Exception {
        initEventReader("<?xml version=\"1.0\"?><tables2ignore><table name=\"test\"/></tables2ignore>");
        List<String> lst = reader.deserializeTableList(inputStream);
        assertNotNull(lst);
        assertEquals(1, lst.size());
        assertEquals("test", lst.get(0));
    }

    @Test
    public void tableListWithTwoTables_deserialize_returnsListWithTwoTableNames() throws Exception {
        initEventReader("<?xml version=\"1.0\"?><tables2ignore><table name=\"test\"/><table name=\"test2\"></table></tables2ignore>");
        List<String> lst = reader.deserializeTableList(inputStream);
        assertNotNull(lst);
        assertEquals(2, lst.size());
        assertEquals("test", lst.get(0));
        assertEquals("test2", lst.get(1));
    }

    @Test(expected = UnsuportedElementException.class)
    public void badTables2IgnoreTagName_deserialize_UnknownElementException() throws Exception {
        initEventReader("<?xml version=\"1.0\"?><tables_2ignore><tableList name=\"test\"/></tables2ignore>");
        reader.deserializeTableList(inputStream);
    }

    @Test(expected = UnsuportedElementException.class)
    public void badTableTagName_deserialize_UnknownElementException() throws Exception {
        initEventReader("<?xml version=\"1.0\"?><tables2ignore><tablesss name=\"test\"/></tables2ignore>");
        reader.deserializeTableList(inputStream);
    }

    @Test(expected = UnknownAttributeException.class)
    public void badTableAttributeName_deserialize_UnknownAttributeException() throws Exception {
        initEventReader("<?xml version=\"1.0\"?><tables2ignore><table nameee=\"test\"/></tables2ignore>");
        reader.deserializeTableList(inputStream);
    }
    private void initEventReader(String xmlText) {
        inputStream = new ByteArrayInputStream(xmlText.getBytes());
    }
}