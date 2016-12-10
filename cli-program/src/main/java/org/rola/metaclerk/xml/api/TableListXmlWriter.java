package org.rola.metaclerk.xml.api;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.rola.metaclerk.model.TableList;

public interface TableListXmlWriter {

	void serializeToXml(TableList tableList, XMLStreamWriter writer, String tagName) throws XMLStreamException;

}