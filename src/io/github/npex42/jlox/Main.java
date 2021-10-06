package io.github.npex42.jlox;

import io.github.npex42.jlox.backend.*;
import io.github.npex42.jlox.backend.visitors.AstPrinter;
import io.github.npex42.jlox.backend.visitors.Interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {


        String src = Files.readString(Paths.get("test.lox"));
        ErrorReporter reporter = new ConsoleReporter();
        Scanner sc = new Scanner(src, reporter);
        List<Token> tokens = sc.scanTokens();

        if (reporter.hadError()) return;

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        if (Parser.reporter.hadError()) {
            return;
        }


        Interpreter interpreter = new Interpreter(reporter);

        interpreter.interpret(statements);

    }
}
