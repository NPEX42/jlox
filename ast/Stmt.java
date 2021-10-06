package io.github.npex42.jlox.backend;

import java.util.List;

public abstract class Stmt {
 public interface Visitor<R> {
  public R visitBlockStmt(Block stmt);
  public R visitExpressionStmt(Expression stmt);
  public R visitPrintStmt(Print stmt);
  public R visitVarStmt(Var stmt);
  public R visitIfStmt(If stmt);
  }
 public static class Block extends Stmt {
 public Block(List<Stmt> statements) {
      this.statements = statements;
    }

 public final List<Stmt> statements;

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitBlockStmt(this);
    }
  }
 public static class Expression extends Stmt {
 public Expression(Expr expression) {
      this.expression = expression;
    }

 public final Expr expression;

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitExpressionStmt(this);
    }
  }
 public static class Print extends Stmt {
 public Print(Expr expression) {
      this.expression = expression;
    }

 public final Expr expression;

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitPrintStmt(this);
    }
  }
 public static class Var extends Stmt {
 public Var(Token name, Expr initializer) {
      this.name = name;
      this.initializer = initializer;
    }

 public final Token name;
 public final Expr initializer;

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitVarStmt(this);
    }
  }
 public static class If extends Stmt {
 public If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
      this.condition = condition;
      this.thenBranch = thenBranch;
      this.elseBranch = elseBranch;
    }

 public final Expr condition;
 public final Stmt thenBranch;
 public final Stmt elseBranch;

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitIfStmt(this);
    }
  }

 public abstract <R> R accept(Visitor<R> visitor);
}
