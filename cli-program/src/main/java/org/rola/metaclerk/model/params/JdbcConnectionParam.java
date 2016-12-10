package org.rola.metaclerk.model.params;

public class JdbcConnectionParam {
    private final String jdbcConnectionStr;
    private final String userName;
    private final String password;
    private final String dbType;

    public JdbcConnectionParam(String jdbcConnectionStr, String userName, String password, String dbType) {
        this.jdbcConnectionStr = jdbcConnectionStr;
        this.userName = userName;
        this.password = password;
        this.dbType = dbType;
    }

    public String getJdbcConnectionStr() {
        return jdbcConnectionStr;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDbType() {
        return dbType;
    }
}
