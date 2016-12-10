package org.rola.metaclerk.xml.impl;


import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.model.PrivilegeList;
import org.rola.metaclerk.xml.api.DbPrivilegeListXmlWriter;
import org.rola.metaclerk.xml.impl.constants.DbPrivilegeConst;

import javax.xml.stream.XMLStreamException;

public class DbPrivilegeListXmlWriterImpl extends BaseXmlWriterImpl<PrivilegeList> implements DbPrivilegeListXmlWriter, DbPrivilegeConst {

    @Override
    protected void serializeObject() throws XMLStreamException {
        if (object.size() <= 0)
            return;
        serializePrivilegeList(object);
    }

    private void serializePrivilegeList(PrivilegeList privList) throws XMLStreamException {
        xmlWriter.writeStartElement(tagName);
        xmlWriter.writeAttribute(COUNT_ATTR, Integer.toString(privList.size()));
        for (DbPrivilege privilege: privList) serializePrivilege(privilege);
        xmlWriter.writeEndElement();
    }

    private void serializePrivilege(DbPrivilege privilege) throws XMLStreamException {
        xmlWriter.writeEmptyElement(PRIVILEGE_TAG);
        xmlWriter.writeAttribute(OBJECT_ATTR, privilege.getName());
        xmlWriter.writeAttribute(OWNER_ATTR, privilege.getOwnerName());
        xmlWriter.writeAttribute(PRIVILEGE_ATTR, notNullStr(privilege.getPrivilege()));
        xmlWriter.writeAttribute(GRANTABLE_ATTR, notNullStr(privilege.getGrantable()));
    }
}
