package org.rola.metaclerk.command.api;

import org.rola.metaclerk.model.params.JdbcConnectionParam;

public interface SnapshotCommand {
	void execute(JdbcConnectionParam jdbcParams, String schemaName, String outputFileName);
}
