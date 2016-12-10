package org.rola.metaclerk.plugin.oracle.dao.impl;

import org.rola.metaclerk.dao.impl.BaseJdbcConnectorImpl;


public class OracleJdbcConnectorImpl extends BaseJdbcConnectorImpl {

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";

	@Override
	protected void safeLoadDriver() throws ClassNotFoundException {
		Class.forName(DB_DRIVER);
	}
}
