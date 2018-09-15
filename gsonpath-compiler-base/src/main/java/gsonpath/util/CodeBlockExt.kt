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

fun CodeBlock.Builder.autoControlFlow(controlFlow: String, vararg args: Any, func: CodeBlock.Builder.() -> Unit): CodeBlock.Builder {
    beginControlFlow(controlFlow, *args)
    func(this)
    endControlFlow()
    return this
}

fun CodeBlock.Builder.ifBlock(
        condition: String,
        vararg args: Any,
        func: CodeBlock.Builder.() -> Unit): CodeBlock.Builder = autoControlFlow("if ($condition)", *args, func = func)

fun CodeBlock.Builder.whileBlock(
        condition: String,
        vararg args: Any,
        func: CodeBlock.Builder.() -> Unit): CodeBlock.Builder = autoControlFlow("while ($condition)", *args, func = func)

fun CodeBlock.Builder.switchBlock(
        condition: String,
        vararg args: Any,
        func: CodeBlock.Builder.() -> Unit): CodeBlock.Builder = autoControlFlow("switch ($condition)", *args, func = func)

fun CodeBlock.Builder.forBlock(
        condition: String,
        vararg args: Any,
        func: CodeBlock.Builder.() -> Unit): CodeBlock.Builder = autoControlFlow("for ($condition)", *args, func = func)
