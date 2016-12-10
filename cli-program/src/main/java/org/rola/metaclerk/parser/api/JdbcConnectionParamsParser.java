package org.rola.metaclerk.parser.api;

import org.rola.metaclerk.model.params.JdbcConnectionParam;
import org.rola.metaclerk.utils.ArrayIterator;

public interface JdbcConnectionParamsParser extends BaseParamParser {
    String JDBC_CONNECTION_PARAM = "-c";
    String DB_USER_PARAM = "-u";
    String DB_PASSWORD_PARAM = "-p";
    String SCHEMA_NAME_PARAM = "-s";
    String DATABASE_TYPE_PARAM = "-d";
    String DEFAULT_DB_TYPE = "oracle";

    void setup(ArrayIterator<String> paramIt);

    boolean parseParam();

    void checkMandatoryParams();

    String getJdbcConnectionStr();

    String getSchemaName();

    String getDbPassword();

    String getDbUser();

    String getDbType();

    JdbcConnectionParam getJdbcConnectionParam();
}
