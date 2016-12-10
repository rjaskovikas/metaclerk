package org.rola.metaclerk.printers.api;

import org.rola.metaclerk.model.diff.TableDifference;

public interface TableDifferencePrinter {
    void print(TableDifference colDiff, MessagePrinter messagePrinter);
}
