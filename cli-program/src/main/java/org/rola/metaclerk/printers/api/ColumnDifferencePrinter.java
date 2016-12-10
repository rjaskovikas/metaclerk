package org.rola.metaclerk.printers.api;

import org.rola.metaclerk.model.diff.ColumnDifference;

public interface ColumnDifferencePrinter {
    void print(ColumnDifference colDiff, MessagePrinter messagePrinter);
}
