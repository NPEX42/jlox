package io.github.npex42.jlox.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.npex42.jlox.backend.TokenType.*;

public class Scanner {

    private static final char NULL = '\0';

    private final String source;
    private final ErrorReporter reporter;
    private List<Token> tokens = new ArrayList<>();
    private int current = 0, start = 0, line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    public Scanner(String source, ErrorReporter reporter) {
        this.source = source;
        this.reporter = reporter;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(matches('-') ? MINUS_MINUS : MINUS); break;
            case '+': addToken(matches('+') ? PLUS_PLUS : PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '%': addToken(PERCENT); break;
            case '=': {
                if (matches('=')) { addToken(EQUAL_EQUAL); }
                else {addToken(EQUAL);}
                break;
            }

            case '>': {
                if (matches('=')) {addToken(GREATER_EQUAL);}
                else {addToken(GREATER);}
                break;
            }

            case '<': {
                if (matches('=')) {addToken(LESS_EQUAL);}
                else {addToken(LESS);}
                break;
            }

            case '!': addToken(matches('=') ? BANG : BANG_EQUAL); break;
            case '/': if (matches('/')) {
                    // Consume Tokens Until End Of Line Or End of File.
                    while (peek() != '\n' && !isAtEnd()) {advance();}
                } else {
                    addToken(SLASH);
                }

            case ' ':
            case '\t':
            case '\r':
                // Ignore Whitespace
                break;
            case '\n':
                line++;
                break;

            case '"':
                string();
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    reporter.report(line, "Unexpected Character: '%c'.", c);
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }



    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }

        if (isAtEnd()) {
            reporter.report(line, "Unterminated String");
        }

        // Consume the Closing '"'
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        value = value.translateEscapes();
        addToken(STRING, value);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(NUMBER,
                Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private char advance() {
        return source.charAt(current++);
    }

    private boolean matches(char chr) {
        return chr == peek();
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char peek() {
        if (isAtEnd()) {
            return NULL;
        } else {
            return source.charAt(current);
        }
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }


}
