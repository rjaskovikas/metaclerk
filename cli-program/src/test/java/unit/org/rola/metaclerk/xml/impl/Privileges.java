package org.rola.metaclerk.xml.impl;

import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.model.PrivilegeList;
import org.rola.metaclerk.model.diff.PrivilegeDiffType;
import org.rola.metaclerk.model.diff.PrivilegeDifference;

public class Privileges {

    static public DbPrivilege createPrivilage(String name, String owner, String privilege, String grantable) {
        DbPrivilege dbPriv = new DbPrivilege();
        dbPriv.setName(name);
        dbPriv.setOwnerName(owner);
        dbPriv.setPrivilege(privilege);
        dbPriv.setGrantable(grantable);
        return dbPriv;
    }

    static public PrivilegeList createPrivilegeList() {
        PrivilegeList lst = new PrivilegeList();
        lst.add(createPrivilage("tableList", "test1", "select", "YES"));
        lst.add(createPrivilage("proc", "test2", "execute", "NO"));
        return lst;
    }

    public static PrivilegeDifference createPrivilegeDifference() {
        return new PrivilegeDifference(
                createPrivilage("name", "owner", "select", "YES"),
                createPrivilage("name", "owner", "select", "NO"),
                PrivilegeDiffType.GRANT);
    }
}
