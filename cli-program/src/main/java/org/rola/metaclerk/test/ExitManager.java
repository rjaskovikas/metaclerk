package org.rola.metaclerk.test;

public class ExitManager {
    private static final ExitManager instance = new ExitManager();

    ExitManager() {
    }

    public static ExitManager getInstance() {
        return instance;
    }

    public void SystemExit(int code) {
        System.exit(code);
    }

}