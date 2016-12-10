package org.rola.metaclerk.command.api;

import org.rola.metaclerk.command.impl.SnapshotCommandImpl;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;
import org.rola.metaclerk.plugin.impl.DynamicDAOFactoryLoaderImpl;
import org.rola.metaclerk.test.CreateObjectFactory;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.SystemStreams;
import org.rola.metaclerk.xml.api.DbPrivilegeListXmlWriter;
import org.rola.metaclerk.xml.api.SchemaXmlWriter;
import org.rola.metaclerk.xml.api.TableListXmlWriter;
import org.rola.metaclerk.xml.impl.DbPrivilegeListXmlWriterImpl;
import org.rola.metaclerk.xml.impl.SchemaXmlWriterImpl;
import org.rola.metaclerk.xml.impl.TableListXmlWriterImpl;

public interface SnapshotCommandFactory extends CreateObjectFactory<SnapshotCommand> {
    static SnapshotCommandFactory createFactory() {
        return () -> {
            DynamicDAOFactoryLoader daoFactoryLoader = new DynamicDAOFactoryLoaderImpl();
            TableListXmlWriter tableWriter = new TableListXmlWriterImpl();
            DbPrivilegeListXmlWriter privWriter = new DbPrivilegeListXmlWriterImpl();
            SchemaXmlWriter xmlWriter = new SchemaXmlWriterImpl();
            return new SnapshotCommandImpl(daoFactoryLoader, xmlWriter, StreamFactory.getInstance(), SystemStreams.getInstance());
        };
    }
}
