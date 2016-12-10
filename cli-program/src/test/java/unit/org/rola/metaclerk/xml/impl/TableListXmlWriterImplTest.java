package org.rola.metaclerk.xml.impl;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.TableList;
import org.rola.metaclerk.model.Tables;
import org.rola.metaclerk.xml.api.TableListXmlWriter;
import org.rola.metaclerk.xml.impl.constants.TableConst;

public class TableListXmlWriterImplTest implements TableConst{
	
	private XMLStreamWriter xmlWriter;
	private StringWriter stringWriter;
	private final TableListXmlWriter wr = new TableListXmlWriterImpl();
	private TableList tableList = new TableList();

	private static final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
	
	@Before
	public void setUp() throws XMLStreamException {
		stringWriter = new StringWriter();
		xmlWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);
		xmlWriter.writeStartDocument();
		xmlWriter.writeStartElement("schema");
	}
	
	@Test(expected=NullPointerException.class)
	public void nullAsTableDescriptionObject_seserializeTableToXml_throwsNullPointerException () throws Exception {
		wr.serializeToXml(null, xmlWriter, "test");
	}
	
	@Test
	public void tableObjectNoComumns_serializeTableToXml_singleTableElement() throws Exception {
		tableList = Tables.createTables("TableOwner", "TableName");
		tableList.get(0).getColumns().clear();
		wr.serializeToXml(tableList, xmlWriter, TABLE_ELEMENT);
		xmlWriter.writeEndDocument();

//		System.out.println(stringWriter.toString());
		assertEquals("<?xml version=\"1.0\" ?>" +
					 "<schema>" +
						"<tables count=\"1\">" +
							"<table name=\"TableName\" owner=\"TableOwner\"/>" +
						"</tables>" +
					"</schema>",
				stringWriter.toString());
	}
	
	@Test
	public void tableObjectWithColumns_serializeTableToXml_CorrectTableXml() throws Exception {
		wr.serializeToXml(Tables.createTables("TableOwner", "TableName"), xmlWriter, TABLE_ELEMENT);
		xmlWriter.writeEndDocument();
		
//		System.messagePrinter.println(stringWriter.toString());
		assertEquals("<?xml version=\"1.0\" ?>" +
						"<schema>" +
						"<tables count=\"1\">" +
							"<table name=\"TableName\" owner=\"TableOwner\">" +
								"<column column_id=\"1\" name=\"Column1\" type=\"NVARCHAR2\" char_used=\"B\" data_length=\"5\" data_precision=\"2\" data_scale=\"6\" data_default=\"SYSDATE\" nullable=\"true\"/>" +
								"<column column_id=\"2\" name=\"Column2\" type=\"NUMBER\" char_used=\"B\" data_length=\"5\" data_precision=\"2\" data_scale=\"6\" data_default=\"15\" nullable=\"false\"/>" +
							"</table>" +
						"</tables>" +
						"</schema>",
				stringWriter.toString());
	}

	@Test
	public void tableColumnsWithOptionalColumnValuesSetToNull_serializeTableToXml_EmptyAttributeValuesForNullValues() throws Exception {
		prepareTableColumnsWithNullValues();
		
		wr.serializeToXml(tableList, xmlWriter, TABLE_ELEMENT);
		xmlWriter.writeEndDocument();
		
		assertEquals("<?xml version=\"1.0\" ?>" +
				"<schema>" +
					"<tables count=\"1\">" +
						"<table name=\"TableName\" owner=\"TableOwner\">" +
							"<column column_id=\"1\" name=\"Column1\" type=\"NVARCHAR2\" char_used=\"\" data_length=\"\" data_precision=\"\" data_scale=\"\" data_default=\"\" nullable=\"true\"/>" +
							"<column column_id=\"2\" name=\"Column2\" type=\"NUMBER\" char_used=\"\" data_length=\"\" data_precision=\"\" data_scale=\"\" data_default=\"\" nullable=\"false\"/>" +
						"</table>" +
					"</tables>" +
				"</schema>",
				stringWriter.toString());
//		System.messagePrinter.println(stringWriter.toString());
	}

	private void prepareTableColumnsWithNullValues() {
		TableDescription table = Tables.createTableWithColumns();
		tableList.add(table);
		for (ColumnDescription c: table.getColumns()) {
			c.setCharUsed(null);
			c.setDataDefault(null);
			c.setDataScale(null);
			c.setDataPrecision(null);
			c.setDataLength(null);
		}
	}
}
