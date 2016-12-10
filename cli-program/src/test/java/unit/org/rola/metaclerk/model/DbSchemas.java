package org.rola.metaclerk.model;

import org.rola.metaclerk.xml.api.SchemaXmlWriter;
import org.rola.metaclerk.xml.impl.Privileges;
import org.rola.metaclerk.xml.impl.SchemaXmlWriterImpl;

import java.io.*;

public class DbSchemas {
    static public DbSchema createDbSchema(String schemaName) {
        DbSchema schema = new DbSchema();
        schema.setName(schemaName);
        schema.setTables(Tables.createTables(schemaName, "tableVienas", "tableDu"));
        schema.setViews(ViewList.formTableList(Tables.createTables(schemaName, "viewVienas", "viewDu")));
        schema.setPrivileges(Privileges.createPrivilegeList());
        return schema;
    }

    public static InputStream createDbSchemaInputStream() {
        try {
            OutputStream buffer = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(buffer);
            SchemaXmlWriter schemaWriter = new SchemaXmlWriterImpl();
            schemaWriter.serializeDbSchema(createDbSchema("test"), printStream);
            return new ByteArrayInputStream(buffer.toString().getBytes());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static InputStream createIgnoreListStream() {
        String buffer = "<?xml version=\"1.0\"?>" +
                          "<tables2ignore>" +
                             "<table name=\"ignore1\"/>" +
                             "<table name=\"ignore2\"></table>" +
                          "</tables2ignore>";
        return new ByteArrayInputStream(buffer.getBytes());
    }
}
