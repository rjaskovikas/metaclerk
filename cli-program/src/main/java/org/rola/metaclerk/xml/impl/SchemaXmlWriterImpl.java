package org.rola.metaclerk.xml.impl;

import org.rola.metaclerk.exception.xml.XMLRuntimeException;
import org.rola.metaclerk.model.DbSchema;
import org.rola.metaclerk.xml.api.DbPrivilegeListXmlWriter;
import org.rola.metaclerk.xml.api.SchemaXmlWriter;
import org.rola.metaclerk.xml.api.TableListXmlWriter;
import org.rola.metaclerk.xml.impl.constants.DbPrivilegeConst;
import org.rola.metaclerk.xml.impl.constants.SchemaConst;
import org.rola.metaclerk.xml.impl.constants.TableConst;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.PrintStream;

public class SchemaXmlWriterImpl implements SchemaXmlWriter, SchemaConst {
	private XMLStreamWriter xmlWriter;
	private DbSchema schema;
	private TableListXmlWriter tableXmlWriter;
	private DbPrivilegeListXmlWriter privXmlWriter;
	private final static XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

	public SchemaXmlWriterImpl() {
		this.tableXmlWriter = new TableListXmlWriterImpl();
		this.privXmlWriter = new DbPrivilegeListXmlWriterImpl();
	}

	void setTableXmlWriter(TableListXmlWriter tableXmlWriter) {
		this.tableXmlWriter = tableXmlWriter;
	}

	void setPrivilegeXmlWriter(DbPrivilegeListXmlWriter privXmlWriter) {
		this.privXmlWriter = privXmlWriter;
	}

	@Override
	public void serializeDbSchema(DbSchema dbSchema, PrintStream printStream)  {
		schema = dbSchema;
		try {
			xmlWriter = xmlOutputFactory.createXMLStreamWriter(printStream);
			serializeDbSchemaSafe();
		} catch (XMLStreamException ex) {
			throw new XMLRuntimeException("Error writing to xml:", ex);
		}
	}

	private void serializeDbSchemaSafe() throws XMLStreamException {
		startXmlDocument();
		serializeSchemaElements();
		endXmlDocument();
	}

	private void serializeSchemaElements() throws XMLStreamException {
		serializeSchemaTables();
		serializeSchemaViews();
		serializePrivileges();
	}

	private void serializePrivileges() throws XMLStreamException {
		privXmlWriter.serializeToXml(schema.getPrivileges(), xmlWriter, DbPrivilegeConst.PRIVILEGES_TAG);
	}

	private void serializeSchemaViews() throws XMLStreamException {
		if (schema.getViews().size() == 0)
			return;
		createXmlViewsBlock();
	}

	private void createXmlViewsBlock() throws XMLStreamException {
		tableXmlWriter.serializeToXml(schema.getViews().toTableList(), xmlWriter, TableConst.VIEW_ELEMENT);
	}

	private void serializeSchemaTables() throws XMLStreamException {
		tableXmlWriter.serializeToXml(schema.getTables(), xmlWriter, TableConst.TABLE_ELEMENT);
	}

	private void startXmlDocument() throws XMLStreamException {
    	xmlWriter.writeStartDocument(XML_ENCODING, XML_VERSION);
		xmlWriter.writeStartElement(SCHEMA_ELEMENT);
		xmlWriter.writeAttribute(VERSION_ATTR, schema.getSchamaVersion());
	}

	private void endXmlDocument() throws XMLStreamException {
		xmlWriter.writeEndElement();
		xmlWriter.writeEndDocument();
		xmlWriter.flush();
		xmlWriter.close();
	}
}