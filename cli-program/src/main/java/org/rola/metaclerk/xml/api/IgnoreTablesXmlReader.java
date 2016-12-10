package org.rola.metaclerk.xml.api;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.List;

public interface IgnoreTablesXmlReader {
    List<String> deserializeTableList(InputStream reader) throws XMLStreamException;
}
