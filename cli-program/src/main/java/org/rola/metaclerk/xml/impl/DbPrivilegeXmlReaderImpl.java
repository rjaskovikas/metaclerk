package org.rola.metaclerk.xml.impl;

import org.rola.metaclerk.exception.xml.MandatoryAttributeValidationException;
import org.rola.metaclerk.exception.xml.UnknownAttributeException;
import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.xml.api.DbPrivilegeXmlReader;
import org.rola.metaclerk.xml.impl.constants.DbPrivilegeConst;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class DbPrivilegeXmlReaderImpl extends BaseXMLReaderImpl implements DbPrivilegeXmlReader, DbPrivilegeConst {

    private DbPrivilege dbPrivilege;

    @Override
    public DbPrivilege deserializePrivilege(XMLEventReader reader, StartElement startElement, String tagName) throws XMLStreamException {
        init(reader, startElement, tagName);
        dbPrivilege = new DbPrivilege();
        readPrivilege();
        return dbPrivilege;
    }

    private void readPrivilege() throws XMLStreamException {
        checkElementTagName();
        forEachElementAttribute(xmlEvent.asStartElement(), this::setColumnAttribute);
        validatePrivilegeAttributes();
        readFromEventReaderTillEndElement();
    }

    private void validatePrivilegeAttributes() {
        boolean dataValid = dbPrivilege.getName() != null
                && dbPrivilege.getOwnerName() != null;
        if (!dataValid)
            throw new MandatoryAttributeValidationException(String.format("DB privilege (%s) is missing mandaroty attribute", dbPrivilege.getName()));
    }

    private void setColumnAttribute(Attribute atr) {
        switch (atr.getName().getLocalPart().toLowerCase()) {
            case OBJECT_ATTR:
                dbPrivilege.setName(atr.getValue());
                break;
            case OWNER_ATTR:
                dbPrivilege.setOwnerName(atr.getValue());
                break;
            case PRIVILEGE_ATTR:
                dbPrivilege.setPrivilege(getStringValue(atr));
                break;
            case GRANTABLE_ATTR:
                dbPrivilege.setGrantable(getStringValue(atr));
                break;
            default:
                throw new UnknownAttributeException("Unsuported column attribute: \"" + atr.getName() + "\"");
        }
    }
}
