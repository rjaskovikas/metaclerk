package org.rola.metaclerk.printers.api;

import org.rola.metaclerk.model.diff.PrivilegeDiffList;

public interface PrivilegeDifferencePrinter {
    void print(PrivilegeDiffList privilegeDiff, MessagePrinter messagePrinter);
}
