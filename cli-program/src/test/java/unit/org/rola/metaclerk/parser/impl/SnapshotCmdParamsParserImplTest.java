package org.rola.metaclerk.parser.impl;

import org.junit.Before;
import org.junit.Test;
import org.rola.metaclerk.command.api.SnapshotCommand;
import org.rola.metaclerk.command.api.SnapshotCommandFactory;
import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.parser.api.JdbcConnectionParamsParser;
import org.rola.metaclerk.parser.api.VerboseLevelParser;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.test.BufferedSystemStreams;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.StringUtils;
import org.rola.metaclerk.test.TestExitManager;
import org.rola.metaclerk.utils.ArrayIterator;

import static org.junit.Assert.*;

public class SnapshotCmdParamsParserImplTest  implements SnapshotCommand  {
	private int executeCommandCallCount;
	private boolean throwJdbcParserCheckMandParamsException;
	private boolean throwVerboseParserCheckMandatoryParamException;
	private SnapshotCmdParamsParserImpl parser;
	private JdbcConnectionParamsParser jdbcParser;
	private VerboseLevelParser verboseParser;
	private final BufferedSystemStreams systemStreams = new BufferedSystemStreams();
	private TestExitManager exitManager = new TestExitManager();
	private String executeOutputFileName;

	@Before
	public void setup() {
		createEmbeddedParsers();
		SnapshotCommandFactory cmdFactory = () -> SnapshotCmdParamsParserImplTest.this;
		parser = new SnapshotCmdParamsParserImpl(cmdFactory, StreamFactory.getInstance(),
				systemStreams, exitManager);
		parser.setJdbcParer(jdbcParser);
		parser.setVerboseParser(verboseParser);
	}

	private void createEmbeddedParsers() {
		jdbcParser = new JdbcConnectionParamsParser() {
			@Override
			public String getParamsDescriptionString() {
				return "";
			}

			@Override
			public String getParamsUsageString() {
				return "";
			}

			@Override
			public void setup(ArrayIterator<String> paramIt) {}

			@Override
			public boolean parseParam() {
				return false; //always return not my param
			}

			@Override
			public void checkMandatoryParams() {
				if (throwJdbcParserCheckMandParamsException)
					throw new BadParamException("Test");
			}

			@Override
			public String getJdbcConnectionStr() {
				return null;
			}

			@Override
			public String getSchemaName() {
				return null;
			}

			@Override
			public String getDbPassword() {
				return null;
			}

			@Override
			public String getDbUser() {
				return null;
			}

			@Override
			public String getDbType() {
				return null;
			}

			@Override
			public JdbcConnectionParam getJdbcConnectionParam() {
				return null;
			}
		};

		verboseParser = new VerboseLevelParser() {
			@Override
			public String getParamsDescriptionString() {
				return "";
			}

			@Override
			public String getParamsUsageString() {
				return "";
			}

			@Override
			public void setup(ArrayIterator<String> paramIt) {

			}

			@Override
			public PrinterVerboseLevel getVerboseLevel() {
				return null;
			}

			@Override
			public boolean parseParam() {
				return false;
			}

			@Override
			public void checkMandatoryParams() {
				if (throwVerboseParserCheckMandatoryParamException)
					throw new BadParamException("test");
			}
		};
	}

	@Test
	public void emptyParamsList_parse_printsUsage() throws Exception {
		throwJdbcParserCheckMandParamsException = true; // missing required jdbc values
		parser.parseAndExecute(new String[0]);
		
		assertEquals(1, getPrintUsageCallCount());
	}
	
	@Test
	public void unknownParam_parse_printsErrorAndUsage() throws Exception {
		parser.parseAndExecute(new String[]{"-unknown"});
		
		assertEquals(1, getPrintUsageCallCount());
		assertEquals(0, executeCommandCallCount);
	}

	@Test
	public void paramWithNoValue_parse_printsErrorAndUsage() throws Exception {
		parser.parseAndExecute(new String[]{"-o"});
		
		assertEquals(1, getPrintUsageCallCount());
		assertEquals(0, executeCommandCallCount);
	}
	

	@Test
	public void allMandatoryAndOptionalParams_parse_executesCommandPrintsToFile() throws Exception {
		parser.parseAndExecute(new String[] {"-o", "file"});
		
		assertEquals(0, getPrintUsageCallCount());
		assertEquals(1, executeCommandCallCount);
		assertEquals("file", parser.outFileName); // checks print to file
	}

	@Test
	public void missingVerboseMandatoryParams_parse_printsCommandUsage() throws Exception {
		throwVerboseParserCheckMandatoryParamException = true; // imitate missing mandatory verbose param
		parser.parseAndExecute(new String[] {"-o", "file"});

		assertEquals(1, getPrintUsageCallCount());
		assertEquals(0, executeCommandCallCount);
	}

	@Test
	public void allMandatoryAndJdbcParams_parse_executesCommandPrintsToStdout() throws Exception {
		throwJdbcParserCheckMandParamsException = false; // imitate that all jdbc params were passed
		parser.parseAndExecute(new String[] {});

		assertEquals(0, getPrintUsageCallCount());
		assertEquals(1, executeCommandCallCount);
		assertNull(parser.outFileName); // checks print to StdOut
	}

	@Test
	public void noParams_parse_fillOutVariableWithStdOut() throws Exception {
		parser.parseAndExecute(new String[] {}); // outFileName is null

		assertEquals(0, getPrintUsageCallCount());
		assertEquals(1, executeCommandCallCount);
		assertNull(executeOutputFileName);
	}

	@Test
	public void helpArgument_parse_printsCommandUsage() throws Exception {
		throwJdbcParserCheckMandParamsException = false; // imitate missing jdbc params
		parser.parseAndExecute(new String[] {"--help"});
		assertEquals(1, getPrintUsageCallCount());
		assertEquals(0, executeCommandCallCount);
		
		systemStreams.clear();
		parser.parseAndExecute(new String[] {"--help", "-c"});
		assertEquals(1, getPrintUsageCallCount());
		assertEquals(0, executeCommandCallCount);

		systemStreams.clear();
		parser.parseAndExecute(new String[] {"--help", "-u", "bb"});
		assertEquals(1, getPrintUsageCallCount());
		assertEquals(0, executeCommandCallCount);
	}

	@Test
	public void equalSchemas_systemExit_callsDbComparatorSystemExitWith0() throws Exception {
		parser.closeProgram();
		assertEquals(0, exitManager.getExitCode());
	}

	@Test
	public void parserObjec_getParamsDescriptionString_returnsDescriptionWihtSnapshotWord() throws Exception {
		assertTrue(parser.getParamsDescriptionString().contains("snapshot"));
	}

	@Test
	public void parserObjec_getParamsUsageString_returnsDescriptionsWith_O_Parameter() throws Exception {
		assertTrue(parser.getParamsUsageString().contains("-o"));
	}

	private int getPrintUsageCallCount() {
		return StringUtils.getWordRepeatCount(systemStreams.getSystemErrBuffer().toString(), "Command usage:");
	}

	@Override
	public void execute(JdbcConnectionParam param, String schemaName, String outputFileName) {
		this.executeCommandCallCount++;
		this.executeOutputFileName = outputFileName;
	}
}
