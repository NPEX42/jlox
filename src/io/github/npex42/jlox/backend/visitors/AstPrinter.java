package io.github.npex42.jlox.backend.visitors;

import io.github.npex42.jlox.backend.Expr;

public class AstPrinter implements Expr.Visitor<String> {

    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return "assign("+expr.name.lexeme()+","+expr.value.accept(this)+")";
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return expr.operator.lexeme()+"("+expr.left.accept(this)+","+expr.right.accept(this)+")";
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return "("+expr.expression.accept(this)+")";
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return "";
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return expr.operator.lexeme()+"("+expr.right.accept(this)+")";
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return "var("+expr.name.lexeme()+")";
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
