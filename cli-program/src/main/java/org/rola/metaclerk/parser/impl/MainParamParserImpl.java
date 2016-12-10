package org.rola.metaclerk.parser.impl;

import org.rola.metaclerk.parser.api.*;
import org.rola.metaclerk.test.SystemStreams;

import java.io.PrintStream;

public class MainParamParserImpl implements MainParamParser {
	BaseCommandParamsParser cmdParser;
	private final CheckCmdParamsParserFactory checkParserFactory;
	private final SnapshotCmdParamsParserFactory snapshotParserFactory;
	private final SystemStreams systemStreams;

	public MainParamParserImpl(CheckCmdParamsParserFactory checkParserFactory, SnapshotCmdParamsParserFactory snapshotParserFactory,
							   SystemStreams systemStreams) {
		this.checkParserFactory = checkParserFactory;
		this.snapshotParserFactory = snapshotParserFactory;
		this.systemStreams = systemStreams;
	}

	@Override
	public void parseAndExecute(String[] params) {
		if (params.length == 0) {
			printProgramUsage();
			return;
		}
		parseAndExecuteCommands(params);
	}

	private void parseAndExecuteCommands(String[] params) {
		switch(params[0].toLowerCase()) {
		case SNAPSHOT_CMD:
			createSnapshotParser().parseAndExecute(cloneFrom(params, 1));
			break;
		case CHECK_CMD:
			createCheckParser().parseAndExecute(cloneFrom(params, 1));
			break;
		default:
			printProgramUsage();
		}
	}

	private String[] cloneFrom(String[] params, int start) {
		String[] copy = new String[params.length - start];
		for (int i=0; start < params.length; start++, i++)
			copy[i] = params[start];
		return copy;
	}

	void printProgramUsage() {
		PrintStream out = systemStreams.getSystemErr();
		out.println("Program usage:");
		out.println("metaclerk [command] {command parameters}");
		out.println();
		out.println("Commands:");
		out.println("\tsnapshot - prints database schema snapshot");
		out.println("\tcheck - checks database schema against database snapshot");
	}

	private SnapshotCmdParamsParser createSnapshotParser() {
		cmdParser = snapshotParserFactory.build();
		return  (SnapshotCmdParamsParser) cmdParser;
	}

	private CheckCommandParamsParser createCheckParser() {
		cmdParser = checkParserFactory.build();
		return (CheckCommandParamsParser) cmdParser;
	}

	@Override
	public void closeProgram() {
		if (cmdParser != null)
			cmdParser.closeProgram();
	}
}
