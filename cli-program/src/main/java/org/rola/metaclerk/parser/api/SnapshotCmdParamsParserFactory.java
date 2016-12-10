package org.rola.metaclerk.parser.api;

import org.rola.metaclerk.command.api.SnapshotCommandFactory;
import org.rola.metaclerk.parser.impl.SnapshotCmdParamsParserImpl;
import org.rola.metaclerk.test.CreateObjectFactory;
import org.rola.metaclerk.test.ExitManager;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.SystemStreams;

public interface SnapshotCmdParamsParserFactory extends CreateObjectFactory<SnapshotCmdParamsParser> {

    static SnapshotCmdParamsParserFactory createFactory() {
        return SnapshotCmdParamsParserFactory::createObject;
    }

    static SnapshotCmdParamsParserImpl createObject() {
        return new SnapshotCmdParamsParserImpl(
                SnapshotCommandFactory.createFactory(),
                StreamFactory.getInstance(),
                SystemStreams.getInstance(),
                ExitManager.getInstance());
    }
}
