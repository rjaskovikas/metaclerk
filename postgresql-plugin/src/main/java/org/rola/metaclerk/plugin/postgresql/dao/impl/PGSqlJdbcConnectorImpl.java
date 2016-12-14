package org.rola.metaclerk.plugin.postgresql.dao.impl;

import org.rola.metaclerk.dao.impl.BaseJdbcConnectorImpl;


public class PGSqlJdbcConnectorImpl extends BaseJdbcConnectorImpl {

	private static final String DB_DRIVER = "org.postgresql.Driver";

	@Override
	protected void safeLoadDriver() throws ClassNotFoundException {
		Class.forName(DB_DRIVER);
	}
}
