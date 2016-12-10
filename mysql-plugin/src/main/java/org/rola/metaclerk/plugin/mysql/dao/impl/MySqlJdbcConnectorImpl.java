package org.rola.metaclerk.plugin.mysql.dao.impl;

import org.rola.metaclerk.dao.impl.BaseJdbcConnectorImpl;


public class MySqlJdbcConnectorImpl extends BaseJdbcConnectorImpl {

	private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

	@Override
	protected void safeLoadDriver() throws ClassNotFoundException {
		Class.forName(DB_DRIVER);
	}
}
