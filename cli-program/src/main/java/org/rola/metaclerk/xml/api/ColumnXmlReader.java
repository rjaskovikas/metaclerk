package org.rola.metaclerk.xml.api;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.rola.metaclerk.model.ColumnDescription;

public interface ColumnXmlReader {
	ColumnDescription deserializeColumn(XMLEventReader reader, StartElement startElement) throws XMLStreamException;
}
