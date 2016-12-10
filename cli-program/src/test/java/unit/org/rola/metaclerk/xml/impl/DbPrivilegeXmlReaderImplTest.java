package org.rola.metaclerk.xml.impl;

import org.junit.Test;
import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.xml.api.DbPrivilegeXmlReader;
import org.rola.metaclerk.exception.xml.MandatoryAttributeValidationException;
import org.rola.metaclerk.exception.xml.UnsuportedElementException;
import org.rola.metaclerk.xml.impl.constants.DbPrivilegeConst;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import java.io.StringReader;

import static org.junit.Assert.*;


public class DbPrivilegeXmlReaderImplTest implements DbPrivilegeConst{
    private final DbPrivilegeXmlReader reader = new DbPrivilegeXmlReaderImpl();
    private static final XMLInputFactory factory = XMLInputFactory.newInstance();
    private XMLEventReader eventReader;
    private DbPrivilege privilege;

    @Test
    public void allAttributes_deserialize_fillsAttributesCorrectly() throws Exception {
        initEventReader("<privilege object=\"a\" owner=\"b\" privilege=\"c\" grantable=\"d\"/>");
        privilege = reader.deserializePrivilege(eventReader, eventReader.nextEvent().asStartElement(), PRIVILEGE_TAG);

        assertEquals("a", privilege.getName());
        assertEquals("b", privilege.getOwnerName());
        assertEquals("c", privilege.getPrivilege());
        assertEquals("d", privilege.getGrantable());
    }

    @Test
    public void emptyOptionalAttributes_desserialize_assignsNullToAttributes() throws Exception {
        initEventReader("<privilege object=\"a\" owner=\"b\" privilege=\"\" grantable=\"\"/>");
        privilege = reader.deserializePrivilege(eventReader, eventReader.nextEvent().asStartElement(), PRIVILEGE_TAG);

        assertEquals("a", privilege.getName());
        assertEquals("b", privilege.getOwnerName());
        assertNull("c", privilege.getPrivilege());
        assertNull(privilege.getGrantable());
    }

    @Test(expected = UnsuportedElementException.class)
    public void badXmlTagName_desserialize_throwsException() throws Exception {
        initEventReader("<privilege1 object=\"a\" owner=\"b\" privilege=\"\" grantable=\"\"/>");
        privilege = reader.deserializePrivilege(eventReader, eventReader.nextEvent().asStartElement(), PRIVILEGE_TAG);
    }

    @Test(expected = MandatoryAttributeValidationException.class)
    public void objectAttributeIsMissing_deserialize_throwAttrIsMissingException() throws Exception {
        initEventReader("<privilege owner=\"owner\"/>");
        reader.deserializePrivilege(eventReader, eventReader.nextEvent().asStartElement(), PRIVILEGE_TAG);
    }

    @Test(expected = MandatoryAttributeValidationException.class)
    public void ownerAttributeIsMissing_deserialize_throwAttrIsMissingException() throws Exception {
        initEventReader("<privilege object=\"name\"/>");
        reader.deserializePrivilege(eventReader, eventReader.nextEvent().asStartElement(), PRIVILEGE_TAG);
    }

    private void initEventReader(String xmlText) throws XMLStreamException {
        eventReader = factory.createXMLEventReader(new StringReader(xmlText));
        eventReader.nextEvent(); // consume start document event
    }

}