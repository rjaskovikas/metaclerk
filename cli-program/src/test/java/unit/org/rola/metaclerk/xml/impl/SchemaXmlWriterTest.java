package org.rola.metaclerk.xml.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.*;
import org.rola.metaclerk.xml.api.DbPrivilegeListXmlWriter;
import org.rola.metaclerk.xml.api.TableListXmlWriter;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class SchemaXmlWriterTest {

	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	private final PrintStream printWriter = new PrintStream(buffer);
	private SchemaXmlWriterImpl schemaWriter;
	private final DbSchema dbs = new DbSchema();
	
	@Before
	public void setUp() throws XMLStreamException {
		TableListXmlWriter tableListXmlWriter = (tableList, writer, tagName) -> {
            if (tableList.size() <=0) return;
            writer.writeStartElement(tagName + 's');
            writer.writeAttribute("count", Integer.toString(tableList.size()));
            for(int i=0; i<tableList.size();i++) writer.writeEmptyElement(tagName);
            writer.writeEndElement();
        };

		DbPrivilegeListXmlWriter privListWriter = new DbPrivilegeListXmlWriterImpl();

		schemaWriter = new SchemaXmlWriterImpl();
		schemaWriter.setTableXmlWriter(tableListXmlWriter);
		schemaWriter.setPrivilegeXmlWriter(privListWriter);

		dbs.setTables(new TableList());
		dbs.setViews(new ViewList());
		dbs.setPrivileges(new PrivilegeList());
	}
	
	@Test
	public void emptySchema_serialize_emptyXmlDoclument() throws Exception {		
		schemaWriter.serializeDbSchema(dbs, printWriter);
		
		assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?><schema version=\"1.0\"></schema>", 
				     buffer.toString());
//		System.messagePrinter.println(stringWriter);
	}
	
	@Test
	public void dbSchemaWithOneDbTableAndOneDbView_serialize_xmlDocumentWithOneTableAndOneView() throws Exception {
		dbs.getTables().add(Tables.createTableWithColumns());
		dbs.getViews().add(ViewDescription.fromTableDescription(Tables.createTableWithColumns()));
		
		schemaWriter.serializeDbSchema(dbs, printWriter);

//		System.out.println(stringWriter);
		assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
						"<schema version=\"1.0\">" +
							"<tables count=\"1\">" +
								"<table/>" +
							"</tables>" +
							"<views count=\"1\">" +
								"<view/>" +
							"</views>" +
						"</schema>",
				     buffer.toString());

	}

	@Test
	public void dbSchemaWithOneTableAndOnePrivilege_serialize_fillsXmlDocumentWithOneTableAndPrivilege() throws Exception {
		dbs.getTables().add(Tables.createTableWithColumns());
		dbs.getPrivileges().add(org.rola.metaclerk.xml.impl.Privileges.createPrivilage("proc", "test3", "select", "NO"));

		schemaWriter.serializeDbSchema(dbs, printWriter);

//		System.out.println(printWriter);
		assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
						"<schema version=\"1.0\">" +
							"<tables count=\"1\"><table/></tables>" +
							"<privileges count=\"1\">" +
								"<privilege object=\"proc\" owner=\"test3\" privilege=\"select\" grantable=\"NO\"/>" +
							"</privileges>" +
						"</schema>",
				buffer.toString());

	}
}
