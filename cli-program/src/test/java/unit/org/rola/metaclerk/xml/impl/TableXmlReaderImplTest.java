package org.rola.metaclerk.xml.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.xml.api.ColumnXmlReader;
import org.rola.metaclerk.exception.xml.MandatoryAttributeValidationException;
import org.rola.metaclerk.exception.xml.UnknownAttributeException;
import org.rola.metaclerk.xml.impl.constants.TableConst;

public class TableXmlReaderImplTest implements TableConst {
	private TableXmlReaderImpl reader;
	static private final XMLInputFactory factory = XMLInputFactory.newInstance();
	private XMLEventReader eventReader;
	private TableDescription table;
	
	@Before
	public void setUp() {
		ColumnXmlReader columnReader = new ColumnXmlReader() {
			private int cnt = 1;
			@Override
			public ColumnDescription deserializeColumn(XMLEventReader reader, StartElement startElement)
					throws XMLStreamException {
				ColumnDescription col = new ColumnDescription();
				col.setName("fakeName"+cnt);
				col.setType("fakeType"+cnt);
				cnt ++;
				reader.nextEvent(); // read document end event;
				return col;
			}
		};
		reader = new TableXmlReaderImpl();
		reader.setColumnReader(columnReader);
	}
	
	@Test (expected=NullPointerException.class)
	public void nullReader_deserialize_throwNullPointerException() throws Exception {
		reader.deserializeTable(null, null, TABLE_ELEMENT);
	}

	@Test
	public void emptyTableElementWithAllAttributes_deserialize_filledTableObject() throws Exception {
		initEventReader("<table name=\"test\" owner=\"testowner\"/>");
		
		table = reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
		
//		System.messagePrinter.println(table.toString());
		assertEquals("test", table.getName());
		assertEquals("testowner", table.getOwnerName());
		assertEquals(0, table.getColumns().size());
	}
	
	@Test
	public void normalTableElementWithAllAttributes_deserialize_filledTableObject() throws Exception {
		initEventReader("<table name=\"test\" owner=\"testowner\"></table>");
		
		table = reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
		
//		System.messagePrinter.println(table.toString());
		assertEquals("test", table.getName());
		assertEquals("testowner", table.getOwnerName());
		assertEquals(0, table.getColumns().size());
	}
	
	@Test
	public void emptyTableElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		initEventReader("<table name=\"test\" owner=\"testowner\"/>");
		
		table = reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, eventReader.nextEvent().getEventType());
		assertFalse(eventReader.hasNext());
		assertEquals(0, table.getColumns().size());
	}
	
	@Test
	public void normalTableElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		initEventReader("<table name=\"test\" owner=\"testowner\"></table>");
		
		table = reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, eventReader.nextEvent().getEventType());
		assertFalse(eventReader.hasNext());
		assertEquals(0, table.getColumns().size());
	}
	
	@Test
	public void normalTableElementWithColumn_deserialize_readsXmlTillNextXmlElement() throws Exception {
		initEventReader("<table name=\"test\" owner=\"testowner\"><column/></table>");
		
		table = reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, eventReader.nextEvent().getEventType());
		assertFalse(eventReader.hasNext());
		assertEquals(1, table.getColumns().size());
//		System.messagePrinter.println(table);
	}
	@Test
	public void normalTableElementWithTwoColumns_deserialize_readsXmlTillNextXmlElement() throws Exception {
		initEventReader("<table name=\"test\" owner=\"testowner\"><column/><column/></table>");
		
		table = reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, eventReader.nextEvent().getEventType());
		assertFalse(eventReader.hasNext());
		assertEquals(2, table.getColumns().size());
//		System.messagePrinter.println(table);
	}
	
	@Test(expected = UnknownAttributeException.class)
	public void tableElementWihUnknownAttr_deserialize_throwUnknownAttrException() throws Exception {
		initEventReader("<table name=\"test\" owner=\"testowner\" someattr=\"aa\"/>");
		reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
	}
	
	@Test(expected = MandatoryAttributeValidationException.class)
	public void tableNameIsMissing_deserialize_throwAttrIsMissingException() throws Exception {
		initEventReader("<table owner=\"testowner\"/>");
		reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
	}
	
	@Test(expected = MandatoryAttributeValidationException.class)
	public void columnTypeIsMissing_deserialize_throwAttrIsMissingException() throws Exception {
		initEventReader("<table name=\"test\"/>");
		reader.deserializeTable(eventReader, eventReader.nextEvent().asStartElement(), TABLE_ELEMENT);
	}

	private void initEventReader(String xmlText) throws XMLStreamException {
		eventReader = factory.createXMLEventReader(new StringReader(xmlText));
		eventReader.nextEvent(); // consume start document event
	}

}
