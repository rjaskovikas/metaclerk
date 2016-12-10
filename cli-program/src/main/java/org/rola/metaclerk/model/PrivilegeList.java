package org.rola.metaclerk.model;

public class PrivilegeList extends DbObjectList<DbPrivilege> {

    public DbPrivilege findPrivilege(DbPrivilege expected) {
        for (DbPrivilege privilege : this) {
            if (!privilege.getOwnerName().equalsIgnoreCase(expected.getOwnerName())) continue;
            if (!privilege.getName().equalsIgnoreCase(expected.getName())) continue;
            if (!privilege.getPrivilege().equalsIgnoreCase(expected.getPrivilege())) continue;
            return privilege;
        }
        return null;
    }

}
