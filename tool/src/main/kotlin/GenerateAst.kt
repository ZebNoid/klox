package org.lox.tool

import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess


object GenerateAst {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val path = Paths.get("").toAbsolutePath().toString()
        println("Working Directory = $path")
        println("args = ${args.joinToString(", ")}")

        if (args.size != 1) {
            println("Usage: generate_ast <output directory>")
            exitProcess(64)
        }
        val outputDir: String = args[0]

        defineAst(outputDir, "Expr", listOf(
            "Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal  : Any? value",
            "Unary    : Token operator, Expr right",
        ))
    }

    @Throws(IOException::class)
    private fun defineAst(outputDir: String, baseName: String, types: List<String>) {
        val path = "$outputDir/$baseName.kt"
        val writer = PrintWriter(path, "UTF-8")

        writer.println("package org.lox.ast")
        writer.println()
        writer.println("import org.lox.token.Token")
        writer.println()
        writer.println("sealed class $baseName {")

        defineVisitor(writer, baseName, types)

        // The AST classes.
        for (type in types) {
            writer.println()
            val className = type.split(":")[0].trim()
            val fields = type.split(":")[1].trim()
            defineType(writer, baseName, className, fields)
        }

        // The base accept() method.
        writer.println()
        writer.print("    ")
        writer.println("abstract fun <R> accept(visitor: Visitor<R>): R")
        writer.println("}")
        writer.close()
    }

    private fun defineVisitor(
        writer: PrintWriter,
        baseName: String,
        types: List<String>
    ) {
        writer.print("    ")
        writer.println("interface Visitor<R> {")

        for (type in types) {
            val typeName = type.split(":")[0].trim()
            writer.print("    ")
            writer.print("    ")
            writer.println("fun visit$typeName$baseName(${baseName.lowercase()}: $typeName): R")
        }

        writer.print("    ")
        writer.println("}")
    }

    private fun defineType(
        writer: PrintWriter,
        baseName: String,
        className: String,
        fieldList: String
    ) {
        writer.print("    ")
        writer.println("data class $className(")
        // fields.
        val fields: List<String> = fieldList.split(", ")
        for (field in fields) {
            val type: String = field.split(" ")[0]
            val name: String = field.split(" ")[1]
            writer.print("    ")
            writer.print("    ")
            writer.println("val $name: $type,")
        }
        writer.print("    ")
        writer.println(") : $baseName() {")

        // Visitor pattern.
        writer.print("    ")
        writer.print("    ")
        writer.println("override fun <R> accept(visitor: Visitor<R>) = visitor.visit$className$baseName(this)")
        writer.print("    ")
        writer.println("}")
    }
}
