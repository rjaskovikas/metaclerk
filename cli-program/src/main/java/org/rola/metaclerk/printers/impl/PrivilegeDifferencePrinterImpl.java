package org.rola.metaclerk.printers.impl;

import org.rola.metaclerk.model.DbPrivilege;
import org.rola.metaclerk.model.diff.PrivilegeDiffList;
import org.rola.metaclerk.model.diff.PrivilegeDifference;
import org.rola.metaclerk.printers.api.MessagePrinter;
import org.rola.metaclerk.printers.api.PrivilegeDifferencePrinter;

public class PrivilegeDifferencePrinterImpl implements PrivilegeDifferencePrinter {
    private static final String SEPARATOR_LINE = "-----------";
    private PrivilegeDiffList privilegeDiffList;
    private MessagePrinter messagePrinter;

    @Override
    public void print(PrivilegeDiffList privilegeDiffList, MessagePrinter messagePrinter) {
        init(privilegeDiffList, messagePrinter);
        printDifferences();
    }

    private void init(PrivilegeDiffList privilegeDiff, MessagePrinter messagePrinter) {
        this.privilegeDiffList = privilegeDiff;
        this.messagePrinter = messagePrinter;
    }

    private void printDifferences() {
        privilegeDiffList.forEach(this::printDifference);
    }

    private void printDifference(PrivilegeDifference diff) {
        switch (diff.getDifference()) {
            case ACTUAL_NEW: printActualNewDifference(diff);
                break;
            case EXPECTED_NOT_FOUNT: printExpectedNotFoundDifference(diff);
                break;
            case GRANT: printGrantDifference(diff);
                break;
            default:
                return;
        }
        messagePrinter.printlnV(SEPARATOR_LINE);
    }

    private void printGrantDifference(PrivilegeDifference diff) {
        String mess = "Different grant option for (grant " +
                diff.getActual().getPrivilege() + " on "  + diff.getActual().getOwnerName() + "."
                + diff.getActual().getName() + ") (snapshot, database)";
        messagePrinter.printlnV(mess);
        messagePrinter.printlnV(diff.getExpected().getGrantable() + ", " + diff.getActual().getGrantable());
    }

    private void printExpectedNotFoundDifference(PrivilegeDifference diff) {
        String mess = "Grant (grant " +
                diff.getExpected().getPrivilege() + " on "  + diff.getExpected().getOwnerName() + "."
                + diff.getExpected().getName() + getGrantOptionString(diff.getExpected()) + ") not found in database schema";
        messagePrinter.printlnV(mess);
    }

    private void printActualNewDifference(PrivilegeDifference diff) {
        String mess = "Grant (grant " +
                diff.getActual().getPrivilege() + " on "  + diff.getActual().getOwnerName() + "."
                + diff.getActual().getName() + getGrantOptionString(diff.getActual()) + ") is new in database schema";
        messagePrinter.printlnV(mess);
    }

    private String getGrantOptionString(DbPrivilege privilege) {
        return privilege.getGrantable().equalsIgnoreCase("YES") ? " with grant option" : "";
    }
}
