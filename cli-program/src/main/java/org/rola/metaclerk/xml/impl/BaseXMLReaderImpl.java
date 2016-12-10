package org.rola.metaclerk.xml.impl;

import org.rola.metaclerk.exception.xml.UnsuportedElementException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.function.Consumer;

class BaseXMLReaderImpl {
	XMLEventReader eventReader;
	XMLEvent xmlEvent;
	private String tagName;

	void init(XMLEventReader reader, XMLEvent event, String tagName) {
		this.eventReader = reader;
		this.xmlEvent = event;
		this.tagName = tagName;
	}

	static String getStringValue(Attribute atr) {
		return atr.getValue().equals("") ? null : atr.getValue();
	}

	static Integer getIntValue(Attribute atr) {
		return atr.getValue().equals("") ? null : Integer.parseInt(atr.getValue());
	}

	static boolean getBoolValue(Attribute atr) {
		return Boolean.parseBoolean(atr.getValue());
	}

	private boolean readNextNot(@SuppressWarnings("SameParameterValue") int stopEventType) throws XMLStreamException {
		if (eventReader.hasNext()) {
			xmlEvent = eventReader.nextEvent();
			if (xmlEvent.getEventType() != stopEventType)
				return true;
		}
		return false;
	}

	boolean readNextNotEndElement() throws XMLStreamException {
		return readNextNot(XMLStreamConstants.END_ELEMENT);
	}
	
	void readFromEventReaderTillEndElement() throws XMLStreamException {
		//noinspection StatementWithEmptyBody
		while (readNextNot(XMLStreamConstants.END_ELEMENT));
	}

	@SuppressWarnings({"raw", "unchecked"})
	void forEachElementAttribute(StartElement element, Consumer<Attribute> consumer) {
		element.getAttributes().forEachRemaining(consumer);
	}

	boolean readNextStartElementEvent() throws XMLStreamException {
		while (readNextNotEndElement()) {
			if (xmlEvent.isStartElement())
				return true;
		}
		return false;
	}

	void checkElementTagName() {
		final String name = xmlEvent.asStartElement().getName().getLocalPart();
		if (!name.equals(tagName))
			throw new UnsuportedElementException("Element '"+tagName+"' is expected instead of '" + name + "'");
	}
}