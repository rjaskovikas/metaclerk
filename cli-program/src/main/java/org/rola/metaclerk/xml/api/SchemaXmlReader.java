package org.rola.metaclerk.xml.api;

import java.io.InputStream;

import org.rola.metaclerk.model.DbSchema;

public interface SchemaXmlReader {
	DbSchema deserializeSchema(InputStream stream) ;
}
