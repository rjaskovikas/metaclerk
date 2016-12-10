package org.rola.metaclerk.printers.api;

import org.rola.metaclerk.model.diff.TableDifference;

public interface TableWarningPrinter {
    public void print(TableDifference tableDiff, MessagePrinter messagePrinter);
}
