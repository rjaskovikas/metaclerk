package org.rola.metaclerk.model;

public class DbSchema {
	
	private TableList tables;
	private ViewList views;
	private PrivilegeList privileges;

	private final String schemaVersion = "1.0";
	private String name;

	public TableList getTables() {
		return tables;
	}

	public void setTables(TableList tables) {
		this.tables = tables;
	}

	public ViewList getViews() {
		return views;
	}

	public void setViews(ViewList views) {
		this.views = views;
	}

	public String getSchamaVersion() {
		return schemaVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PrivilegeList getPrivileges() {
		return privileges;
	}

	public void setPrivileges(PrivilegeList privileges) {
		this.privileges = privileges;
	}

	@Override
	public String toString() {
		return "DbSchema [name=" + name + ", schemaVersion=" + schemaVersion + ", tables=" + tables
				+ ", views=" + views + "privileges=" + privileges +"]";
	}
}
