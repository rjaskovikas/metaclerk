package org.rola.metaclerk.parser.api;


import org.rola.metaclerk.command.api.CheckCommandFactory;
import org.rola.metaclerk.parser.impl.CheckCmdParamsParserImp;
import org.rola.metaclerk.printers.impl.MessagePrinterImpl;
import org.rola.metaclerk.test.CreateObjectFactory;
import org.rola.metaclerk.test.ExitManager;
import org.rola.metaclerk.test.SystemStreams;

public interface CheckCmdParamsParserFactory extends CreateObjectFactory<CheckCommandParamsParser> {

    static CheckCmdParamsParserFactory createFactory() {
        return CheckCmdParamsParserFactory::createObject;
    }

    static CheckCmdParamsParserImp createObject() {
        return new CheckCmdParamsParserImp(
                MessagePrinterImpl.getDefaultPrinter(),
                CheckCommandFactory.createFactory(),
                SystemStreams.getInstance(),
                ExitManager.getInstance());
    }
}
