package org.rola.metaclerk.dao.api;

import org.rola.metaclerk.model.PrivilegeList;

public interface DbPrivilegesDAO extends DAOBaseOperations {
    PrivilegeList getDbUserPrivilegeList(String dbUserName);
}
