package org.rola.metaclerk.command.api;

import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;

public interface CheckCommand {
    void execute(JdbcConnectionParam jdbcParams, String schemaName, String inputFileName, String ignoreListFileName,
                 PrinterVerboseLevel verboseLevel);

    boolean isSchemasEqual();
}
