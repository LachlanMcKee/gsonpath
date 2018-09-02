package gsonpath.util

import com.squareup.javapoet.CodeBlock

fun CodeBlock.Builder.addWithNewLine(format: String, vararg args: Any): CodeBlock.Builder {
    this.add(format, *args)
    this.addNewLine()
    return this
}

fun CodeBlock.Builder.addNewLine(): CodeBlock.Builder {
    this.add("\n")
    return this
}

fun CodeBlock.Builder.addComment(comment: String): CodeBlock.Builder {
    this.add("// $comment\n")
    return this
}

fun CodeBlock.Builder.addEscapedStatement(format: String): CodeBlock.Builder {
    this.addStatement(format.replace("$", "$$"))
    return this
}