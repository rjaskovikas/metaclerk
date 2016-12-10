package org.rola.metaclerk.xml.impl;

import org.rola.metaclerk.exception.xml.MandatoryAttributeValidationException;
import org.rola.metaclerk.exception.xml.UnknownAttributeException;
import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.xml.api.ColumnXmlReader;
import org.rola.metaclerk.xml.impl.constants.ColumnConst;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class ColumnXmlReaderImpl extends BaseXMLReaderImpl implements ColumnXmlReader, ColumnConst {
	private ColumnDescription column;
	@Override
	public ColumnDescription deserializeColumn(XMLEventReader reader, StartElement startElement) throws XMLStreamException {
		column = new ColumnDescription();
		init (reader, startElement, COLUMN_ELEMENT);
		readColumnDescription();
		return column;
	}

	private void readColumnDescription() throws XMLStreamException {
		checkElementTagName();
		forEachElementAttribute(xmlEvent.asStartElement(), this::setColumnAttribute);
		validateColumnAttributes();
		readFromEventReaderTillEndElement();
	}

	private void validateColumnAttributes() {
		boolean dataValid = column.getName() != null 
						&& column.getType() != null
						&& column.getColumnID() != null;
		if (!dataValid)
			throw new MandatoryAttributeValidationException(String.format("Column (%s) is missing mandatory attribute", column.getName()));
	}

	private void setColumnAttribute(Attribute atr) {
		switch (atr.getName().getLocalPart().toLowerCase()) {
		case COLUMN_ID_ATTR:
			column.setColumnID(getIntValue(atr));
			break;
		case COLUMN_NAME_ATTR: 
			column.setName(getStringValue(atr)); 
			break;
		case COLUMN_TYPE_ATTR:
			column.setType(getStringValue(atr));
			break;		
		case DATA_LENGTH_ATTR:
			column.setDataLength(getIntValue(atr));
			break;		
		case CHAR_USED_ATTR:
			column.setCharUsed(getStringValue(atr));
			break;
		case NULLABLE_ATTR:
			column.setNullable(getBoolValue(atr));
			break;
		case DATA_SCALE_ATTR:
			column.setDataScale(getIntValue(atr));
			break;
		case DATA_PRECISION_ATTR:
			column.setDataPrecision(getIntValue(atr));
			break;
		case DATA_DEFAULT_ATTR:
			column.setDataDefault(getStringValue(atr));
			break;
		default:
			throw new UnknownAttributeException("Unsupported column attribute: \"" + atr.getName() + "\"");
		}
	}

}
