package org.lox.tool

import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Paths
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
            "Literal  : Any value",
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

        // The AST classes.
        for ((index, type) in types.withIndex()) {
            val className = type.split(":")[0].trim()
            val fields = type.split(":")[1].trim()
            defineType(writer, baseName, className, fields)
            if (index != types.lastIndex) {
                writer.println()
            }
        }

        writer.println("}")
        writer.close()
    }

    private fun defineType(
        writer: PrintWriter, baseName: String?,
        className: String?, fieldList: String
    ) {
        writer.println("    data class $className(")
        // fields.
        val fields: List<String> = fieldList.split(", ")
        for (field in fields) {
            val type: String = field.split(" ")[0]
            val name: String = field.split(" ")[1]
            writer.println("      val $name: $type,")
        }
        writer.println("    ) : $baseName()")
    }
}
