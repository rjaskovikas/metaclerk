package org.rola.metaclerk;

import org.rola.metaclerk.parser.api.CheckCmdParamsParserFactory;
import org.rola.metaclerk.parser.api.MainParamParser;
import org.rola.metaclerk.parser.api.SnapshotCmdParamsParserFactory;
import org.rola.metaclerk.parser.impl.MainParamParserImpl;
import org.rola.metaclerk.test.SystemStreams;

public class MetaClerk {

	MainParamParser mainParser;

	public static void main(String[] args) {
		new MetaClerk().run(args);
	}

	void run(String[] args) {
		createMainParamParser();
		mainParser.parseAndExecute(args);
		mainParser.closeProgram();
	}

	void createMainParamParser() {
		mainParser = new MainParamParserImpl(
				CheckCmdParamsParserFactory.createFactory(),
				SnapshotCmdParamsParserFactory.createFactory(),
				SystemStreams.getInstance()
		);
	}
}
