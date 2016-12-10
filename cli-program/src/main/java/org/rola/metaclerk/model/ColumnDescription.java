package org.rola.metaclerk.model;

public class ColumnDescription extends DbObject {
	private String type;
	private Integer dataLength;
	private Integer dataPrecision;
	private Integer dataScale;
	private Integer columnID;
	private String dataDefault;
	private String charUsed;
	private boolean nullable;

	public String getCharUsed() {
		return charUsed;
	}
	public Integer getColumnID() {
		return columnID;
	}


	@Override
	public String toString() {
		return "ColumnDescription [name=" + getName() + ", type=" + type + ", dataLength=" + dataLength + ", dataPrecision="
				+ dataPrecision + ", dataScale=" + dataScale + ", columnID=" + columnID + ", dataDefault=" + dataDefault
				+ ", charUsed=" + charUsed + ", nullable=" + nullable + "]";
	}

	public void setColumnID(Integer columnID) {
		this.columnID = columnID;
	}

	public void setCharUsed(String charUsed) {
		this.charUsed = charUsed;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getDataLength() {
		return dataLength;
	}
	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
	}
	public Integer getDataPrecision() {
		return dataPrecision;
	}
	public void setDataPrecision(Integer dataPrecision) {
		this.dataPrecision = dataPrecision;
	}
	public Integer getDataScale() {
		return dataScale;
	}
	public void setDataScale(Integer dataScale) {
		this.dataScale = dataScale;
	}
	public String getDataDefault() {
		return dataDefault;
	}
	public void setDataDefault(String dataDefault) {
		this.dataDefault = dataDefault;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public String getObjectType() {
		return ColumnDescription.getStaticTypeName();
	}

	@SuppressWarnings("SameReturnValue")
	public static String getStaticTypeName() {
		return "Column";
	}


	//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		result = prime * result + ((type == null) ? 0 : type.hashCode());
//		return result;
//	}
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		ColumnDescription other = (ColumnDescription) obj;
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
//		if (type == null) {
//			if (other.type != null)
//				return false;
//		} else if (!type.equals(other.type))
//			return false;
//		return true;
//	}

}
