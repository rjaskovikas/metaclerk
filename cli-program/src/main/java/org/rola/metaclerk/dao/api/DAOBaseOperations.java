package org.rola.metaclerk.dao.api;

public interface DAOBaseOperations {
	void initDAO(String jdbcConnectionStr, String dbUser, String dbPassword);
	void closeDAO();
}
