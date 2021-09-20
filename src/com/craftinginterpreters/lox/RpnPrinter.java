package com.craftinginterpreters.lox;

public class RpnPrinter implements Expr.Visitor<String> {

    public static void main(String[] args) {
        Expr expr = new Expr.Binary(
                new Expr.Grouping(
                        new Expr.Binary(
                                new Expr.Literal(1),
                                new Token(TokenType.MINUS, "+", null, 1),
                                new Expr.Literal(2)
                        )),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(new Expr.Binary(
                        new Expr.Literal(4),
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(3)
                ))
        );

        System.out.println(new RpnPrinter().print(expr));
    }

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return rpn(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return rpn("", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return rpn(expr.operator.lexeme, expr.right);
    }

    private String rpn(String name, Expr... exprs) {
        StringBuilder sb = new StringBuilder();

        for (Expr expr : exprs) {
            sb.append(" ");
            sb.append(expr.accept(this));
        }
        sb.append(" ").append(name);

        return sb.toString();
    }
}
