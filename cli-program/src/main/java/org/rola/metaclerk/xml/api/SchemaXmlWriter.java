package org.rola.metaclerk.xml.api;

import org.rola.metaclerk.model.DbSchema;

import java.io.PrintStream;

public interface SchemaXmlWriter {

	void serializeDbSchema(DbSchema dbSchema, PrintStream printStream);

}