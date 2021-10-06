package io.github.npex42.jlox.backend;

import io.github.npex42.jlox.backend.visitors.RuntimeError;

public abstract class ErrorReporter {
    protected boolean hadError = false;
    public abstract void report(int line, String msg, Object... args);

    public boolean hadError() {
        return hadError;
    }

    public abstract void reportRuntimeError(RuntimeError error);
}
