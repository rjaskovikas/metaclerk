package org.rola.metaclerk.xml.impl;

import javax.xml.stream.XMLStreamException;

import org.rola.metaclerk.model.ColumnDescription;
import org.rola.metaclerk.model.TableDescription;
import org.rola.metaclerk.model.TableList;
import org.rola.metaclerk.xml.api.TableListXmlWriter;
import org.rola.metaclerk.xml.impl.constants.ColumnConst;
import org.rola.metaclerk.xml.impl.constants.TableConst;

public class TableListXmlWriterImpl extends BaseXmlWriterImpl<TableList> implements TableListXmlWriter, TableConst, ColumnConst {

	private TableDescription table;

	@Override
	protected void serializeObject() throws XMLStreamException {
		if (object.size() > 0)
			serializeTableList();
	}

	private void serializeTableList() throws XMLStreamException {
		xmlWriter.writeStartElement(tagName+'s');
		xmlWriter.writeAttribute(COUNT_ATTR, Integer.toString(object.size()));
		for (TableDescription table : object) serializeTable(table);
		xmlWriter.writeEndElement();
	}

	private void serializeTable(TableDescription table) throws XMLStreamException {
		this.table = table;
		if (table.getColumns() == null || table.getColumns().size() == 0)
			serializeEmptyTableElement();
		else
			serializeFullTableElement();
	}

	private void serializeFullTableElement() throws XMLStreamException {
		xmlWriter.writeStartElement(tagName);
		writeAttributes();
		writeColumns();
		xmlWriter.writeEndElement();
	}

	private void serializeEmptyTableElement() throws XMLStreamException {
		xmlWriter.writeEmptyElement(tagName);
		writeAttributes();
	}

	private void writeAttributes() throws XMLStreamException {
		xmlWriter.writeAttribute(TABLE_NAME_ATTR, table.getName());
		xmlWriter.writeAttribute(TABLE_OWNER_ATTR, table.getOwnerName());
	}

	private void writeColumns() throws XMLStreamException {
		for (ColumnDescription c : table.getColumns())
			writeColumn(c);
	}

	private void writeColumn(ColumnDescription c) throws XMLStreamException {
		xmlWriter.writeEmptyElement(COLUMN_ELEMENT);
		xmlWriter.writeAttribute(COLUMN_ID_ATTR, 		c.getColumnID().toString());
		xmlWriter.writeAttribute(COLUMN_NAME_ATTR, 		c.getName());
		xmlWriter.writeAttribute(COLUMN_TYPE_ATTR, 		c.getType());
		xmlWriter.writeAttribute(CHAR_USED_ATTR, 		notNullStr(c.getCharUsed()));
		xmlWriter.writeAttribute(DATA_LENGTH_ATTR, 		notNullStr(c.getDataLength()));
		xmlWriter.writeAttribute(DATA_PRECISION_ATTR, 	notNullStr(c.getDataPrecision()));
		xmlWriter.writeAttribute(DATA_SCALE_ATTR, 		notNullStr(c.getDataScale()));
		xmlWriter.writeAttribute(DATA_DEFAULT_ATTR, 	notNullStr(c.getDataDefault()));
		xmlWriter.writeAttribute(NULLABLE_ATTR, 		notNullStr(c.isNullable()));
	}

}
