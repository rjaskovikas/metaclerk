package org.rola.metaclerk.xml.api;

import org.rola.metaclerk.model.PrivilegeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public interface DbPrivilegeListXmlWriter {

    void serializeToXml(PrivilegeList privList, XMLStreamWriter writer, String tagName) throws XMLStreamException;

}
