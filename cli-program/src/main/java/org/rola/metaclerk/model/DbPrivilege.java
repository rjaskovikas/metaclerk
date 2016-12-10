package org.rola.metaclerk.model;

public class DbPrivilege extends DbObject {
    private String privilege;
    private String grantable;

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getGrantable() {
        return grantable;
    }

    public void setGrantable(String grantable) {
        this.grantable = grantable;
    }

    @Override
    public String getObjectType() {
        return "Privilege";
    }

    @Override
    public String toString() {
        return "DbPrivilege{" + super.toString() +
                ", privilege='" + privilege + '\'' +
                ", grantable='" + grantable + '\'' +
                '}';
    }
}
