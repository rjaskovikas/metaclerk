package org.rola.metaclerk.parser.impl;

import org.rola.metaclerk.command.api.CheckCommand;
import org.rola.metaclerk.command.api.CheckCommandFactory;
import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.parser.api.CheckCommandParamsParser;
import org.rola.metaclerk.parser.api.JdbcConnectionParamsParser;
import org.rola.metaclerk.parser.api.VerboseLevelParser;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.test.SystemStreams;
import org.rola.metaclerk.test.ExitManager;

public class CheckCmdParamsParserImp extends BaseCommandParamsParserImpl implements CheckCommandParamsParser {
    String inputFileName;
    private JdbcConnectionParamsParser jdbcPar;
    private VerboseLevelParser verbosePar;
    private CheckCommand cmd;
    String ignoreListFileName;
    private final CheckCommandFactory checkCommandFactory;

    public CheckCmdParamsParserImp(MessagePrinter messagePrinter, CheckCommandFactory checkCommandFactory,
                                   SystemStreams systemStreams, ExitManager exitManager) {
        super(systemStreams, exitManager);
        this.jdbcPar = new JdbcConnectionParamsParserImpl();
        this.verbosePar = new VerboseLevelParserImpl();
        this.checkCommandFactory = checkCommandFactory;
    }

    public void setJdbcParser(JdbcConnectionParamsParser jdbcPar) {
        this.jdbcPar = jdbcPar;
    }

    public void setVerboseParser(VerboseLevelParser verbosePar) {
        this.verbosePar = verbosePar;
    }

    @Override
    public void closeProgram() {
        exitManager.SystemExit(cmd != null && cmd.isSchemasEqual() ? 0 : 1);
    }

    protected void executeCommand() {
        createCheckCommand();
        cmd.execute(jdbcPar.getJdbcConnectionParam(), jdbcPar.getSchemaName(), inputFileName, ignoreListFileName,
                verbosePar.getVerboseLevel());
    }

    private void createCheckCommand() {
        cmd = checkCommandFactory.build();
    }

    protected void checkMandatoryParams() {
        if (printHelp)
            return;
        jdbcPar.checkMandatoryParams();
        verbosePar.checkMandatoryParams();
    }

    protected void parseNextParam() throws BadParamException {
        switch (paramIt.current()) {
            case INPUT_FILE_NAME_PARAM:
                parseInputFileName();
                break;
            case IGNORE_LIST_FILE_NAME_PARAM:
                parseIgnoreListFileName();
                break;
            default:
                super.parseNextParam();
        }
    }

    private void parseIgnoreListFileName() {
        checkHasNext("Missing ignore list file name");
        ignoreListFileName = paramIt.next();
    }

    private void parseInputFileName() throws BadParamException {
        checkHasNext("Missing input file name");
        inputFileName = paramIt.next();
    }

    protected boolean isEmbeddedParserParam() {
        return jdbcPar.parseParam() || verbosePar.parseParam();
    }

    protected void setupEmbeddedParsers() {
        jdbcPar.setup(paramIt);
        verbosePar.setup(paramIt);
    }

    @Override
    public String getParamsDescriptionString() {
        return "metaclerk check " + verbosePar.getParamsDescriptionString() + " " +
                jdbcPar.getParamsDescriptionString() + " {-i filename} {-il filename}";
    }

    @Override
    public String getParamsUsageString() {
        return verbosePar.getParamsUsageString() +
                jdbcPar.getParamsUsageString() +
                "\t-i\t - snapshot file name. If parameter not specified command reads data from stdin\n" +
                "\t-il\t - ignore list file name\n";
    }
}
