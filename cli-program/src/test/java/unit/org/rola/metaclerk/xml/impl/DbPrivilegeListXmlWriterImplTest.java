package org.rola.metaclerk.xml.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.model.PrivilegeList;
import org.rola.metaclerk.xml.api.DbPrivilegeListXmlWriter;
import org.rola.metaclerk.xml.impl.constants.DbPrivilegeConst;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class DbPrivilegeListXmlWriterImplTest implements DbPrivilegeConst {
    private XMLStreamWriter xmlWriter;
    private StringWriter stringWriter;
    private final DbPrivilegeListXmlWriter wr = new DbPrivilegeListXmlWriterImpl();
    private PrivilegeList lst;

    private static final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

    @Before
    public void setUp() throws XMLStreamException {
        stringWriter = new StringWriter();
        xmlWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);
        xmlWriter.writeStartDocument();
        xmlWriter.writeStartElement("schema");
    }

    @Test(expected=NullPointerException.class)
    public void nullAsTableDescriptionObject_serializeToXml_throwsNullPointerException () throws Exception {
        wr.serializeToXml(null, xmlWriter, PRIVILEGES_TAG);
    }

    @Test
    public void emptyPrivilegeList_serialize_doesntSerialize() throws Exception {
        lst = new PrivilegeList();
        wr.serializeToXml(lst, xmlWriter, PRIVILEGES_TAG+"_A");
        finishXmlWriting();

        assertEquals("<?xml version=\"1.0\" ?><schema></schema>", stringWriter.toString());
    }

    @Test
    public void privilegeList_serialize_putsListToXml() throws Exception {
        lst = Privileges.createPrivilegeList();
        wr.serializeToXml(lst, xmlWriter, PRIVILEGES_TAG);
        finishXmlWriting();

        assertEquals("<?xml version=\"1.0\" ?>" +
                        "<schema>" +
                            "<privileges count=\"2\">" +
                                "<privilege object=\"tableList\" owner=\"test1\" privilege=\"select\" grantable=\"YES\"/>" +
                                "<privilege object=\"proc\" owner=\"test2\" privilege=\"execute\" grantable=\"NO\"/>" +
                            "</privileges>" +
                        "</schema>",
                stringWriter.toString());
    }

    @Test
    public void privilegeListWithNullPrivilegeAttributes_serialize_putsNullAttributesAsEmptyStrings() throws Exception {
        lst = Privileges.createPrivilegeList();
        DbPrivilege priv = lst.get(0);
        priv.setGrantable(null);
        priv.setPrivilege(null);
        wr.serializeToXml(lst, xmlWriter, PRIVILEGES_TAG+"_A");
        finishXmlWriting();

        assertEquals("<?xml version=\"1.0\" ?>" +
                        "<schema>" +
                        "<privileges_A count=\"2\">" +
                        "<privilege object=\"tableList\" owner=\"test1\" privilege=\"\" grantable=\"\"/>" +
                        "<privilege object=\"proc\" owner=\"test2\" privilege=\"execute\" grantable=\"NO\"/>" +
                        "</privileges_A>" +
                        "</schema>",
                stringWriter.toString());
    }

    private void finishXmlWriting() throws XMLStreamException {
        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();
        xmlWriter.flush();
    }}
