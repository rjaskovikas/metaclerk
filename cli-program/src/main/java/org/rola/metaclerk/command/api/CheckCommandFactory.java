package org.rola.metaclerk.command.api;

import org.rola.metaclerk.command.impl.CheckCommandImpl;
import org.rola.metaclerk.plugin.api.DynamicDAOFactoryLoader;
import org.rola.metaclerk.plugin.impl.DynamicDAOFactoryLoaderImpl;
import org.rola.metaclerk.printers.api.SchemaDifferencePrinter;
import org.rola.metaclerk.printers.impl.SchemaDifferencePrinterImpl;
import org.rola.metaclerk.test.CreateObjectFactory;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.SystemStreams;
import org.rola.metaclerk.xml.api.SchemaXmlReader;
import org.rola.metaclerk.xml.impl.SchemaXmlReaderImpl;

public interface CheckCommandFactory extends CreateObjectFactory<CheckCommand> {

    static CheckCommandFactory createFactory() {
        return () -> {
            DynamicDAOFactoryLoader daoFactoryLoader = new DynamicDAOFactoryLoaderImpl();
            SchemaXmlReader xmlReader = new SchemaXmlReaderImpl();
            SchemaDifferencePrinter diffPrinter = new SchemaDifferencePrinterImpl();
            return new CheckCommandImpl(daoFactoryLoader, xmlReader, diffPrinter, StreamFactory.getInstance(),
                    SystemStreams.getInstance());
        };
    }
}
