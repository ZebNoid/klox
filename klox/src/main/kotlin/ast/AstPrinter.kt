package org.lox.ast

import org.lox.environment.Environment

class AstPrinter(
    private val environment: Environment = Environment()
) :
    Expr.Visitor<String>,
    Stmt.Visitor<String>
{
    fun print(statements: List<Stmt>): String {
        return statements.joinToString("\n") { it.accept(this) }
    }

    fun print(expr: Expr?): String {
        return expr?.accept<String>(this) ?: "On expression"
    }

    override fun visitBinaryExpr(expr: Expr.Binary): String {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right)
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): String {
        return parenthesize("group", expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): String {
        if (expr.value == null) return "nil"
        return expr.value.toString()
    }

    override fun visitUnaryExpr(expr: Expr.Unary): String {
        return parenthesize(expr.operator.lexeme, expr.right)
    }

    override fun visitVariableExpr(expr: Expr.Variable): String {
        return expr.name.lexeme
    }

    override fun visitAssignExpr(expr: Expr.Assign): String {
        return "(assign ${expr.name.lexeme} ${expr.value.accept(this)})"
    }

    private fun parenthesize(name: String, vararg args: Expr): String {
        val builder = StringBuilder()

        builder.append("(").append(name)
        for (expr in args) {
            builder.append(" ")
            builder.append(expr.accept(this))
        }
        builder.append(")")

        return builder.toString()
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression): String {
        return parenthesize("expr", stmt.expression)
    }

    override fun visitPrintStmt(stmt: Stmt.Print): String {
        return parenthesize("print", stmt.expression)
    }

    override fun visitVarStmt(stmt: Stmt.Var): String {
        return "(var ${stmt.name.lexeme} ${stmt.initializer?.accept(this) ?: "nil"})"
    }
}