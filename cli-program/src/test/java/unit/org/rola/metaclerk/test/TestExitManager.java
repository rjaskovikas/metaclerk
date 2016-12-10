package org.rola.metaclerk.test;

public class TestExitManager extends ExitManager {
    private int exitCode;

    public TestExitManager() {
        this.exitCode = -1;
    }

    public int getExitCode() {
        return exitCode;
    }

    @Override
    public void SystemExit(int code) {
        this.exitCode = code;
    }
}
