package org.rola.metaclerk.parser.impl;

import org.rola.metaclerk.command.api.SnapshotCommand;
import org.rola.metaclerk.command.api.SnapshotCommandFactory;
import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.parser.api.JdbcConnectionParamsParser;
import org.rola.metaclerk.parser.api.SnapshotCmdParamsParser;
import org.rola.metaclerk.parser.api.VerboseLevelParser;
import org.rola.metaclerk.test.StreamFactory;
import org.rola.metaclerk.test.SystemStreams;
import org.rola.metaclerk.test.ExitManager;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class SnapshotCmdParamsParserImpl extends BaseCommandParamsParserImpl implements SnapshotCmdParamsParser {

    private final SnapshotCommandFactory snapshotCommandFactory;
    String outFileName;
    private JdbcConnectionParamsParser jdbcParser;
    private VerboseLevelParser verboseParser;
    private SnapshotCommand cmd;
    private StreamFactory streamFactory;

    public SnapshotCmdParamsParserImpl(SnapshotCommandFactory snapshotCommandFactory,
                                       StreamFactory streamFactory, SystemStreams systemStreams, ExitManager exitManager) {
        super(systemStreams, exitManager);
        this.snapshotCommandFactory = snapshotCommandFactory;
        this.jdbcParser = new JdbcConnectionParamsParserImpl();
        this.verboseParser = new VerboseLevelParserImpl();
        this.streamFactory = streamFactory;
    }

    void setJdbcParer(JdbcConnectionParamsParser jdbcPar) {
        this.jdbcParser = jdbcPar;
    }

    void setVerboseParser(VerboseLevelParser verboseParser) {
        this.verboseParser = verboseParser;
    }

    @Override
    public void closeProgram() {
        exitManager.SystemExit(0);
    }

    protected void executeCommand() {
        createSnapshotCommand();
        JdbcConnectionParam jdbcConParam = jdbcParser.getJdbcConnectionParam();
        cmd.execute(jdbcConParam, jdbcParser.getSchemaName(), outFileName);

    }

    private void createSnapshotCommand() {
        cmd = snapshotCommandFactory.build();
    }

    private PrintStream getPrintStream() throws FileNotFoundException {
        if (outFileName == null)
            return systemStreams.getSystemOut();
        return streamFactory.createPrintStream(outFileName);
    }

    protected void checkMandatoryParams() {
        if (printHelp)
            return;
        jdbcParser.checkMandatoryParams();
        verboseParser.checkMandatoryParams();
    }

    protected void parseNextParam() throws BadParamException {
        switch (paramIt.current()) {
            case OUTPUT_FILE_NAME_PARAM:
                parseOutputFileName();
                break;
            default:
                super.parseNextParam();
        }
    }

    private void parseOutputFileName() throws BadParamException {
        checkHasNext("Missing output file name");
        outFileName = paramIt.next();
    }

    protected boolean isEmbeddedParserParam() {
        return jdbcParser.parseParam() || verboseParser.parseParam();
    }

    protected void setupEmbeddedParsers() {
        jdbcParser.setup(paramIt);
        verboseParser.setup(paramIt);
    }


    @Override
    public String getParamsDescriptionString() {
        return "metaclerk snapshot " + verboseParser.getParamsDescriptionString()
                + " " + jdbcParser.getParamsDescriptionString()
                + " {-o filename}";
    }

    @Override
    public String getParamsUsageString() {
        return verboseParser.getParamsUsageString() +
                jdbcParser.getParamsUsageString() +
                "\t-o \t - file name to write snapshot to.\n";
    }
}
