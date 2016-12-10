package org.rola.metaclerk.xml.impl;

import org.rola.metaclerk.exception.xml.UnknownAttributeException;
import org.rola.metaclerk.exception.xml.UnsuportedElementException;
import org.rola.metaclerk.exception.xml.UnsuportedSchemaVersionException;
import org.rola.metaclerk.exception.xml.XMLRuntimeException;
import org.rola.metaclerk.model.*;
import org.rola.metaclerk.xml.api.DbPrivilegeXmlReader;
import org.rola.metaclerk.xml.api.SchemaXmlReader;
import org.rola.metaclerk.xml.api.TableXmlReader;
import org.rola.metaclerk.xml.impl.constants.DbPrivilegeConst;
import org.rola.metaclerk.xml.impl.constants.SchemaConst;
import org.rola.metaclerk.xml.impl.constants.TableConst;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import java.io.InputStream;

public class SchemaXmlReaderImpl extends BaseXMLReaderImpl implements SchemaXmlReader, SchemaConst, TableConst {
	private DbSchema schema;
	private TableXmlReader tableReader;
	private DbPrivilegeXmlReader privilegeReader;

	static final private XMLInputFactory factory = XMLInputFactory.newInstance();

	public SchemaXmlReaderImpl() {
		this.tableReader = new TableXmlReaderImpl();
		this.privilegeReader = new DbPrivilegeXmlReaderImpl();
	}

	void setTableReader(TableXmlReader tableReader) {
		this.tableReader = tableReader;
	}

	void setPrivilegeReader(DbPrivilegeXmlReader privilegeReader) {
		this.privilegeReader = privilegeReader;
	}

	public DbSchema deserializeSchema(InputStream stream) {
		try {
			return deserializeSchemaSafe(stream);
		}
		catch (XMLStreamException ex) {
			throw new XMLRuntimeException("Error:", ex);
		}
	}

	private DbSchema deserializeSchemaSafe(InputStream stream) throws XMLStreamException {
		createEventReaderAndReadTillSchemaStartElement(stream);
		init(eventReader, xmlEvent , SCHEMA_ELEMENT);
		createAndInitDbSchema();
		readSchemaFromXml();
		eventReader.close();
		return schema;
	}

	private void createEventReaderAndReadTillSchemaStartElement(InputStream stream) throws XMLStreamException {
		eventReader = factory.createXMLEventReader(stream);
		eventReader.nextEvent(); // consume start_document xmlEvent
		readNextStartElementEvent();
	}

	private void createAndInitDbSchema() {
		schema = new DbSchema();
		schema.setTables(new TableList());
		schema.setViews(new ViewList());
		schema.setPrivileges(new PrivilegeList());
	}

	private void readSchemaFromXml() throws XMLStreamException {
		checkElementTagName();
		forEachElementAttribute(xmlEvent.asStartElement(), this::setSchemaAttribute);
		readSchemaInnerElementFromXml();
	}

	private void readSchemaInnerElementFromXml() throws XMLStreamException {
		while (readNextStartElementEvent()){
			switch(xmlEvent.asStartElement().getName().getLocalPart().toLowerCase()) {
				case TABLES_ELEMENT:
					readAllTableElements();
					break;
				case VIEWS_ELEMENT:
					readAllViewElements();
					break;
				case DbPrivilegeConst.PRIVILEGES_TAG:
					readAllPrivilegeElements();
					break;
				default:
					throw new UnsuportedElementException("Unknown schema element: " + xmlEvent.asStartElement().getName());
			}
		}
	}

	private void readAllPrivilegeElements() throws XMLStreamException {
		while(readNextStartElementEvent()) {
			schema.getPrivileges().add(
					privilegeReader.deserializePrivilege(eventReader, xmlEvent.asStartElement(), DbPrivilegeConst.PRIVILEGE_TAG));
		}
	}

	private void readAllViewElements() throws XMLStreamException {
		while(readNextStartElementEvent()) {
			ViewDescription view = ViewDescription.fromTableDescription(
					tableReader.deserializeTable(eventReader, xmlEvent.asStartElement(), VIEW_ELEMENT));
			schema.getViews().add(view);
		}
	}

	private void readAllTableElements() throws XMLStreamException {
		while(readNextStartElementEvent()) {
			schema.getTables().add(tableReader.deserializeTable(eventReader, xmlEvent.asStartElement(), TABLE_ELEMENT));
		}
	}

	private void setSchemaAttribute(Attribute atr) {
		switch(atr.getName().getLocalPart().toLowerCase()) {
		case VERSION_ATTR:
			if (!schema.getSchamaVersion().equals(getStringValue(atr))) {
				throw new UnsuportedSchemaVersionException(String.format("Version %s not suported.", atr.getValue()));
			}
			break;
		default:
			throw new UnknownAttributeException("Unsuported column attribute: \"" + atr.getName() + "\"");
		}
	}

}
