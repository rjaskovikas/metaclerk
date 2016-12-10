package org.rola.metaclerk.xml.api;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.rola.metaclerk.model.TableDescription;

public interface TableXmlReader {
	TableDescription deserializeTable(XMLEventReader reader, StartElement startElement, String tagName) throws XMLStreamException;
}
