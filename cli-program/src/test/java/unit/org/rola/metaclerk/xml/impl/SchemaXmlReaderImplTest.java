package org.rola.metaclerk.xml.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.exception.xml.UnknownAttributeException;
import org.rola.metaclerk.exception.xml.UnsuportedElementException;
import org.rola.metaclerk.exception.xml.UnsuportedSchemaVersionException;
import org.rola.metaclerk.exception.xml.XMLRuntimeException;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.xml.api.DbPrivilegeXmlReader;
import org.rola.metaclerk.xml.api.TableXmlReader;

public class SchemaXmlReaderImplTest {
	private SchemaXmlReaderImpl reader;
	static protected XMLInputFactory factory = XMLInputFactory.newInstance();
	protected XMLEventReader eventReader;
	private DbSchema schema;
	private int privilegeReaderCallCount;

	@Before
	public void setUp() {
		TableXmlReader tableReader = new TableXmlReader() {
			int cnt = 1;
			@Override
			public TableDescription deserializeTable(XMLEventReader reader, StartElement startElement, String tagName)
					throws XMLStreamException {
				TableDescription table = new TableDescription();
				table.setName("test" + cnt);
				table.setOwnerName("testOwner");
				reader.nextEvent(); // read message END_ELEMENT xmlEvent;
				cnt ++;
				return table;
			}
		};

		DbPrivilegeXmlReader privilegeReader = (reader1, startElement, tagName) -> {
            reader1.nextEvent();
            privilegeReaderCallCount++; // read message END_ELEMENT xmlEvent
            return null;
        };

		reader = new SchemaXmlReaderImpl();
		reader.setTableReader(tableReader);
		reader.setPrivilegeReader(privilegeReader);
	}
	
	@Test (expected=XMLRuntimeException.class)
	public void nullReader_deserialize_throwXMLStreamException() throws Exception {
		reader.deserializeSchema(null);
	}

	@Test
	public void emptySchemaElementWithAllAttributes_deserialize_filledSchemaObject() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\"/>"));
		
		assertNotNull(schema);
		assertNotNull(schema.getTables());
		assertNotNull(schema.getViews());
	}
	
	@Test
	public void normalSchemaElementWithAllAttributes_deserialize_filledTableObject() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\"></schema>"));
		
		assertNotNull(schema);
		assertEquals(0, schema.getTables().size());
	}
	
	@Test
	public void emptySchemaElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\"/>"));
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, reader.eventReader.nextEvent().getEventType());
		assertFalse(reader.eventReader.hasNext());
		assertEquals(0, schema.getTables().size());
	}
	
	private InputStream makeStream(String xmlText) {
		return new ByteArrayInputStream(xmlText.getBytes());
	}

	@Test
	public void normalSchemaElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\"></schema>"));
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, reader.eventReader.nextEvent().getEventType());
		assertFalse(reader.eventReader.hasNext());
		assertEquals(0, schema.getTables().size());
	}
	
	@Test
	public void normalSchemaElementWithEmptyTablesElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\"><tables/></schema>"));
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, reader.eventReader.nextEvent().getEventType());
		assertFalse(reader.eventReader.hasNext());
		assertEquals(0, schema.getTables().size());
		assertEquals(0, schema.getViews().size());
//		System.messagePrinter.println(schema);
	}
	
	@Test
	public void normalSchemaElementWithTablesElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\"><tables><table/><table/></tables></schema>"));
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, reader.eventReader.nextEvent().getEventType());
		assertFalse(reader.eventReader.hasNext());
		assertEquals(2, schema.getTables().size());
		assertEquals(0, schema.getViews().size());
//		System.out.println(schema);
	}

	@Test
	public void normalSchemaElementWithViewsElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\"><views><view/><view/></views></schema>"));

		assertEquals(XMLStreamConstants.END_DOCUMENT, reader.eventReader.nextEvent().getEventType());
		assertFalse(reader.eventReader.hasNext());
		assertEquals(0, schema.getTables().size());
		assertEquals(2, schema.getViews().size());
//		System.out.println(schema);
	}

	@Test
	public void normalSchemaElementWithViewsAndTableElements_deserialize_readsTwoViewsAndOneTableObject() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\"><tables><table/></tables><views><view/><view/></views></schema>"));

		assertEquals(XMLStreamConstants.END_DOCUMENT, reader.eventReader.nextEvent().getEventType());
		assertFalse(reader.eventReader.hasNext());
		assertEquals(1, schema.getTables().size());
		assertEquals(2, schema.getViews().size());
//		System.out.println(schema);
	}

	@Test
	public void schemaElementWithPrivilegesAndTableElements_deserialize_readsTwoViewsAndOneTableObject() throws Exception {
		schema = reader.deserializeSchema(makeStream("<schema version=\"1.0\">" +
				"<tables>" +
					"<table/>" +
				"</tables>" +
				"<privileges>" +
					"<privilege/>" +
					"<privilege/>" +
				"</privileges>" +
				"</schema>"));

		assertEquals(XMLStreamConstants.END_DOCUMENT, reader.eventReader.nextEvent().getEventType());
		assertFalse(reader.eventReader.hasNext());
		assertEquals(1, schema.getTables().size());
		assertEquals(0, schema.getViews().size());
		assertEquals(2, this.privilegeReaderCallCount);
//		System.out.println(schema);
	}

	@Test(expected = UnsuportedSchemaVersionException.class)
	public void tableElementWihUnknownAttr_deserialize_throwUnknownAttrException() throws Exception {
		reader.deserializeSchema(makeStream("<schema version=\"0.0\"><tables><table/><table/></tables></schema>"));
	}
	
	@Test(expected = UnknownAttributeException.class)
	public void tableNameIsMissing_deserialize_throwAttrIsMissingException() throws Exception {
		reader.deserializeSchema(makeStream("<schema version=\"1.0\" some=\"aa\"><tables><table/><table/></tables></schema>"));
	}

	@Test(expected = UnsuportedElementException.class)
	public void xmlWithUnknownElement_deserialize_throwAttrIsMissingException() throws Exception {
		reader.deserializeSchema(makeStream("<schema version=\"1.0\"><unknown_element/><tables><table/><table/></tables></schema>"));
	}
}
