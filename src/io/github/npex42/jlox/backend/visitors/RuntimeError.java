package io.github.npex42.jlox.backend.visitors;

import io.github.npex42.jlox.backend.Token;

public class RuntimeError extends RuntimeException {
    final Token token;

    public RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }

    @Override
    public String toString() {
        return "Runtime Error: ln "+token.line()+" | "+token.lexeme()+" | "+super.getMessage();
    }
}
