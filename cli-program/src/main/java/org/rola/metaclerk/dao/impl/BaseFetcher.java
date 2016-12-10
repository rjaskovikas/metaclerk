package org.rola.metaclerk.dao.impl;

import org.rola.metaclerk.dao.api.JdbcConnector;

public class BaseFetcher {

	protected JdbcConnector dbc;
	
	protected void setConnector(JdbcConnector con) {
		dbc = con;
	}
}