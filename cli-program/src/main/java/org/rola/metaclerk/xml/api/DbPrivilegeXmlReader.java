package org.rola.metaclerk.xml.api;

import org.rola.metaclerk.model.DbPrivilege;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

public interface DbPrivilegeXmlReader {
    DbPrivilege deserializePrivilege(XMLEventReader reader, StartElement startElement, String tagName) throws XMLStreamException;
}
