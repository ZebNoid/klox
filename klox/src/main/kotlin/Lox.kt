package org.lox

import org.lox.ast.AstPrinter
import org.lox.ast.Expr
import org.lox.ast.Stmt
import org.lox.interpreter.Interpreter
import org.lox.interpreter.RuntimeError
import org.lox.parser.Parser
import org.lox.scanner.Scanner
import org.lox.token.Token
import org.lox.token.TokenType
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess


object Lox {
    val interpreter: Interpreter = Interpreter()
    var hadError: Boolean = false
    var hadRuntimeError: Boolean = false

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size > 1) {
            println("Usage: klox [script]")
            exitProcess(64)
        } else if (args.size == 1) {
            runFile(args[0])
        } else {
            runPrompt()
        }
    }

    @Throws(IOException::class)
    private fun runFile(path: String) {
        val bytes = Files.readAllBytes(Paths.get(path))
        run(String(bytes, Charset.defaultCharset()))

        // Indicate an error in the exit code.
        // TODO https://man.freebsd.org/cgi/man.cgi?query=sysexits&apropos=0&sektion=0&manpath=FreeBSD+4.3-RELEASE&format=html
        if (hadError) exitProcess(65)
        if (hadRuntimeError) exitProcess(70)
    }

    @Throws(IOException::class)
    private fun runPrompt() {
        val input = InputStreamReader(System.`in`)
        val reader = BufferedReader(input)

        while (true) {
            print("> ")
            val line = reader.readLine() ?: break
            run(line)
            hadError = false
        }
    }


    private fun run(source: String) {
        val scanner: Scanner = Scanner(source)
        val tokens: List<Token> = scanner.scanTokens()

        val parser: Parser = Parser(tokens)
        val statements: List<Stmt> = parser.parse()

        // Stop if there was a syntax error.
        if (hadError) return

        interpreter.interpret(statements)
        println(AstPrinter().print(statements))
//        println(AstPrinter().print(expression))
//        println(tokens.joinToString("\n"))
    }

    fun error(line: Int, message: String) {
        report(line, "", message)
    }

    fun runtimeError(error: RuntimeError) {
        println("${error.message}\n[line ${error.token.line}]")
        hadRuntimeError = true
    }

    private fun report(
        line: Int,
        where: String,
        message: String
    ) {
        println("[line $line] Error $where: $message")
        hadError = true
    }

    fun error(token: Token, message: String) {
        if (token.type === TokenType.EOF) {
            report(token.line, " at end", message)
        } else {
            report(token.line, " at '" + token.lexeme + "'", message)
        }
    }
}