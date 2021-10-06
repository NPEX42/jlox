package io.github.npex42.jlox.backend;

public record Token(TokenType type, String lexeme, Object literal, int line) {
    public String toString() {
        return "[" + type + ", " + lexeme + ", " + literal + "]";
    }

    @Override
    public TokenType type() {
        return type;
    }

    @Override
    public String lexeme() {
        return lexeme;
    }

    @Override
    public Object literal() {
        return literal;
    }

    @Override
    public int line() {
        return line;
    }
}
