package org.rola.metaclerk.parser.impl;


import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.parser.api.JdbcConnectionParamsParser;
import org.rola.metaclerk.utils.ArrayIterator;

public class JdbcConnectionParamsParserImpl extends BaseParamParserImpl implements JdbcConnectionParamsParser {

    private String jdbcConnectionStr;
    private String schemaName;
    private String dbPassword;
    private String dbUser;
    private String dbType;

    @Override
    public void setup (ArrayIterator<String> paramIt) {
        setupFields(paramIt);
    }

    @Override
    public boolean parseParam()
    {
        return parseCurrentParam();
    }

    @Override
    public void checkMandatoryParams() {
        checkMandatoryParam(jdbcConnectionStr, "JDBC connection string is missing");
        checkMandatoryParam(dbUser, "Database user name is missing");
        checkMandatoryParam(dbPassword, "Database user password is missing");
        checkMandatoryParam(schemaName, "Database schema name is missing");
    }

    private boolean parseCurrentParam()  {
        switch (paramIt.current()) {
            case JDBC_CONNECTION_PARAM:
                parseJdbcConnectionString();
                return true;
            case DB_USER_PARAM:
                parseDbUser();
                return true;
            case DB_PASSWORD_PARAM:
                parseDbPassword();
                return true;
            case SCHEMA_NAME_PARAM:
                parseSchemaName();
                return true;
            case DATABASE_TYPE_PARAM:
                parseDatabaseType();
                return true;
        }
        return false;
    }

    private void setupFields(ArrayIterator<String> paramIt) {
        this.paramIt = paramIt;
        jdbcConnectionStr = null;
        schemaName = null;
        dbPassword = null;
        dbUser = null;
        dbType = DEFAULT_DB_TYPE;
    }

    private void parseSchemaName()  {
        checkHasNext("Missing schema name");
        schemaName = paramIt.next();
    }

    private void parseDbPassword() {
        checkHasNext("Missing db password");
        dbPassword = paramIt.next();
    }

    private void parseDbUser()  {
        checkHasNext("Missing db user name");
        dbUser = paramIt.next();
    }

    private void parseJdbcConnectionString()  {
        checkHasNext("Missing jdbc connection string");
        jdbcConnectionStr = paramIt.next();
    }

    private void parseDatabaseType() {
        checkHasNext("Missing database type string");
        dbType = paramIt.next();
    }

    @Override
    public String getJdbcConnectionStr() {
        return jdbcConnectionStr;
    }

    @Override
    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public String getDbPassword() {
        return dbPassword;
    }

    @Override
    public String getDbUser() {
        return dbUser;
    }

    @Override
    public String getDbType() {
        return dbType;
    }

    @Override
    public JdbcConnectionParam getJdbcConnectionParam() {
        return new JdbcConnectionParam(jdbcConnectionStr, dbUser, dbPassword, dbType);
    }

    @Override
    public String getParamsDescriptionString() {
        return "-c 'jdbc connection' -u 'db user name' -p  'db password' -s 'schema name' [-d 'database type']";
    }

    @Override
    public String getParamsUsageString() {
        return  "\t-c\t - JDBC connection to actual database string\n" +
                "\t-u\t - connection to database user name\n" +
                "\t-p\t - connection to database user password\n" +
                "\t-s\t - database schema name\n" +
                "\t-d\t - database type, optional parameter. Value 'oracle' used if not specified\n";
    }
}
