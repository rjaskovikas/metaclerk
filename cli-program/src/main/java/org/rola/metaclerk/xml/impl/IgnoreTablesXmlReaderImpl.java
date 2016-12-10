package org.rola.metaclerk.xml.impl;

import org.rola.metaclerk.exception.xml.UnknownAttributeException;
import org.rola.metaclerk.exception.xml.UnsuportedElementException;
import org.rola.metaclerk.xml.api.IgnoreTablesXmlReader;
import org.rola.metaclerk.xml.impl.constants.IgnoreTablesConst;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class IgnoreTablesXmlReaderImpl extends BaseXMLReaderImpl implements IgnoreTablesXmlReader, IgnoreTablesConst {
    private ArrayList<String> tblList;
    static private final XMLInputFactory factory = XMLInputFactory.newInstance();

    @Override
    public List<String> deserializeTableList(InputStream reader) throws XMLStreamException {
        init(reader);
        try {
            readIgnoredTablesElementFromXml();
        } finally {
            this.eventReader.close();
        }
        return tblList;
    }

    private void readIgnoredTablesElementFromXml() throws XMLStreamException {
        eventReader.nextEvent(); // read out start document
        readNextStartElementEvent();
        if (xmlEvent.asStartElement().getName().getLocalPart().toLowerCase().equals(TABLES_2_IGNORE_TAG))
            readTableElementsFromXml();
        else
            throw new UnsuportedElementException("Unknown element: " + xmlEvent.asStartElement().getName().getLocalPart());
    }

    private void readTableElementsFromXml() throws XMLStreamException {
        while (readNextStartElementEvent()) {
            readTable();
        }
    }

    private void readTable() throws XMLStreamException {
        if (xmlEvent.asStartElement().getName().getLocalPart().toLowerCase().equals(TABLE_TAG)) {
            forEachElementAttribute(xmlEvent.asStartElement(), this::readElementAttr);
            readFromEventReaderTillEndElement();
        } else
            throw new UnsuportedElementException("Unknown element: " + xmlEvent.asStartElement().getName().getLocalPart());
    }

    private void init(InputStream reader) throws XMLStreamException {
        this.eventReader = factory.createXMLEventReader(reader);
        tblList = new ArrayList<>();
    }

    private void readElementAttr(Attribute atr) {
        if (atr.getName().getLocalPart().equals(TABLE_NAME_ATTR))
            tblList.add(atr.getValue());
        else
            throw new UnknownAttributeException("Unknown attribute: "+ atr.getName().getLocalPart());
    }
}
