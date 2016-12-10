package org.rola.metaclerk.xml.impl;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.rola.metaclerk.exception.xml.MandatoryAttributeValidationException;
import org.rola.metaclerk.exception.xml.UnknownAttributeException;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.xml.api.ColumnXmlReader;
import org.rola.metaclerk.xml.api.TableXmlReader;
import org.rola.metaclerk.xml.impl.constants.TableConst;

public class TableXmlReaderImpl extends BaseXMLReaderImpl implements TableXmlReader, TableConst {
	private TableDescription table;
	private ColumnXmlReader columnReader;
	protected String tagName;

	public TableXmlReaderImpl() {
		this.columnReader = new ColumnXmlReaderImpl();
	}

	void setColumnReader(ColumnXmlReader columnReader) {
		this.columnReader = columnReader;
	}

	@Override
	public TableDescription deserializeTable(XMLEventReader reader, StartElement startElement, String tagName)
			throws XMLStreamException {
		init(reader, startElement, tagName);
		table = new TableDescription();
		readTable();
		return table;
	}

	private void readTable() throws XMLStreamException {
		readTableAttributes();
		readTableColumns();
	}

	private void readTableAttributes() {
		checkElementTagName();
		forEachElementAttribute(xmlEvent.asStartElement(), this::setColumnAttribute);
		validateTableAttributes();
	}

	private void validateTableAttributes() {
		boolean dataValid = table.getName() != null
							&& table.getOwnerName() != null;
		if (!dataValid)
			throw new MandatoryAttributeValidationException(String.format("Table/View (%s) is missing mandaroty attribute", table.getName()));
	}

	private void readTableColumns() throws XMLStreamException {
		table.getColumns().clear();
		while (readNextNotEndElement()) {
			if (xmlEvent.isStartElement())
				readColumnFromXml();
		}
	}

	private void readColumnFromXml() throws XMLStreamException {
		ColumnDescription col = columnReader.deserializeColumn(eventReader, xmlEvent.asStartElement());
		table.getColumns().add(col);
	}

	private void setColumnAttribute(Attribute atr) {
		switch (atr.getName().getLocalPart().toLowerCase()) {
		case TABLE_NAME_ATTR:
			table.setName(getStringValue(atr));
			break;
		case TABLE_OWNER_ATTR:
			table.setOwnerName(getStringValue(atr));
			break;
		default:
			throw new UnknownAttributeException("Unsuported column attribute: \"" + atr.getName() + "\"");
		}
	}

}
