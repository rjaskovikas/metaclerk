package org.rola.metaclerk.xml.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.xml.api.ColumnXmlReader;
import org.rola.metaclerk.exception.xml.MandatoryAttributeValidationException;
import org.rola.metaclerk.exception.xml.UnknownAttributeException;

public class ColumnXmlReaderImplTest {
	private final ColumnXmlReader reader = new ColumnXmlReaderImpl();
	private static final XMLInputFactory factory = XMLInputFactory.newInstance();
	private XMLEventReader eventReader;
	private ColumnDescription column;
	
	@Test (expected=NullPointerException.class)
	public void nullReader_deserialize_throwNullPointerException() throws Exception {
		reader.deserializeColumn(null, null);
	}

	@Test
	public void columnElementWithAllAttributesBoolsTrue_deserialize_filledColumnObject() throws Exception {
		initEventReader("<column name=\"test\" type=\"nvarchar2\" data_length=\"5\" data_precision=\"1\" data_scale=\"2\" column_id=\"3\" data_default=\"dd\" char_used=\"B\" nullable=\"true\" />");
		
		column = reader.deserializeColumn(eventReader, eventReader.nextEvent().asStartElement());
		
//		System.messagePrinter.println(column.toString());
		assertEquals("test", column.getName());
		assertEquals("nvarchar2", column.getType());
		assertEquals(new Integer(5), column.getDataLength());
		assertEquals(new Integer(1), column.getDataPrecision());
		assertEquals(new Integer(2), column.getDataScale());
		assertEquals(new Integer(3), column.getColumnID());
		assertEquals("dd", column.getDataDefault());
		assertEquals("B", column.getCharUsed());
		assertTrue(column.isNullable());
	}

	@Test
	public void columnElementWithAllAttributesBoolsFalse_deserialize_filledColumnObject() throws Exception {
		initEventReader("<column name=\"test\" type=\"nvarchar2\" data_length=\"5\" data_precision=\"1\" data_scale=\"2\" column_id=\"3\" data_default=\"dd\" char_used=\"B\" nullable=\"false\" />");

		column = reader.deserializeColumn(eventReader, eventReader.nextEvent().asStartElement());

//		System.messagePrinter.println(column.toString());
		assertEquals("test", column.getName());
		assertEquals("nvarchar2", column.getType());
		assertEquals(new Integer(5), column.getDataLength());
		assertEquals(new Integer(1), column.getDataPrecision());
		assertEquals(new Integer(2), column.getDataScale());
		assertEquals(new Integer(3), column.getColumnID());
		assertEquals("dd", column.getDataDefault());
		assertEquals("B", column.getCharUsed());
		assertFalse(column.isNullable());
	}

	@Test
	public void emptyColumnElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		initEventReader("<column name=\"test\" type=\"nvarchar2\" data_length=\"5\" data_precision=\"1\" data_scale=\"2\" column_id=\"3\" data_default=\"dd\" char_used=\"B\" nullable=\"false\" />");
		
		column = reader.deserializeColumn(eventReader, eventReader.nextEvent().asStartElement());
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, eventReader.nextEvent().getEventType());
		assertFalse(eventReader.hasNext());
	}
	
	@Test
	public void fullColumnElement_deserialize_readsXmlTillNextXmlElement() throws Exception {
		initEventReader("<column name=\"test\" type=\"nvarchar2\" data_length=\"5\" data_precision=\"1\" data_scale=\"2\" column_id=\"3\" data_default=\"dd\" char_used=\"B\" nullable=\"false\">aa</column>");
		
		column = reader.deserializeColumn(eventReader, eventReader.nextEvent().asStartElement());
		
		assertEquals(XMLStreamConstants.END_DOCUMENT, eventReader.nextEvent().getEventType());
		assertFalse(eventReader.hasNext());
	}
	
	@Test(expected = MandatoryAttributeValidationException.class)
	public void columnNameIsMissing_deserialize_throwAttrIsMissingException() throws Exception {
		initEventReader("<column type=\"nvarchar2\" data_length=\"5\" data_precision=\"\" data_scale=\"\" column_id=\"3\" data_default=\"\" char_used=\"\" nullable=\"false\" />");
		reader.deserializeColumn(eventReader, eventReader.nextEvent().asStartElement());
	}
	
	@Test(expected = MandatoryAttributeValidationException.class)
	public void columnIdIsMissing_deserialize_throwAttrIsMissingException() throws Exception {
		initEventReader("<column name=\"test\" type=\"nvarchar2\" data_length=\"5\" data_precision=\"\" data_scale=\"\" data_default=\"\" char_used=\"\" nullable=\"false\" />");
		reader.deserializeColumn(eventReader, eventReader.nextEvent().asStartElement());
	}
	
	@Test(expected = MandatoryAttributeValidationException.class)
	public void columnTypeIsMissing_deserialize_throwAttrIsMissingException() throws Exception {
		initEventReader("<column name=\"test\" data_length=\"5\" data_precision=\"\" data_scale=\"\" column_id=\"3\" data_default=\"\" char_used=\"\" nullable=\"false\" />");
		reader.deserializeColumn(eventReader, eventReader.nextEvent().asStartElement());
	}

	@Test(expected = UnknownAttributeException.class)
	public void columnWithUnknownAttribute_deserialize_throwUnknownAttributeException() throws Exception {
		initEventReader("<column nameeee=\"test\" data_length=\"5\" data_precison=\"\" data_scale=\"\" column_id=\"3\" data_default=\"\" char_used=\"\" nullable=\"false\" />");
		reader.deserializeColumn(eventReader, eventReader.nextEvent().asStartElement());
	}

	private void initEventReader(String xmlText) throws XMLStreamException {
		eventReader = factory.createXMLEventReader(new StringReader(xmlText));
		eventReader.nextEvent(); // consume start document event
	}
}
