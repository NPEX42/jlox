package io.github.npex42.jlox.backend;

import io.github.npex42.jlox.backend.visitors.RuntimeError;

public class ConsoleReporter extends ErrorReporter {
    @Override
    public void report(int line, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        System.err.printf("[ERROR|ln %d]: %s%n", line, msg);
        hadError = true;
    }

    @Override
    public void reportRuntimeError(RuntimeError error) {
        System.err.println(error);
    }
}
