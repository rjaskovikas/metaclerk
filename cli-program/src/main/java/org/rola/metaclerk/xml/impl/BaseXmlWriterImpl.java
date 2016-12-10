package org.rola.metaclerk.xml.impl;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

abstract class BaseXmlWriterImpl <T> {
    XMLStreamWriter xmlWriter;
    String tagName;
    T object;

    private void init(T object, XMLStreamWriter xmlWriter, String tagName) {
        this.object = object;
        this.xmlWriter = xmlWriter;
        this.tagName = tagName;
    }


    public void serializeToXml(T object, XMLStreamWriter writer, String tagName) throws XMLStreamException {
        init(object, writer, tagName);
        serializeObject();
    }

    String notNullStr(Object value) {
        return value == null ? "" : value.toString().trim();
    }

    protected abstract void serializeObject() throws XMLStreamException;
}
