package gsonpath.util

import com.squareup.javapoet.CodeBlock

fun CodeBlock.Builder.applyAndBuild(func: CodeBlock.Builder.() -> Unit): CodeBlock {
    return apply(func).build()
}

fun codeBlock(func: CodeBlock.Builder.() -> Unit): CodeBlock {
    return CodeBlock.builder().applyAndBuild(func)
}

fun CodeBlock.Builder.addWithNewLine(format: String, vararg args: Any): CodeBlock.Builder {
    this.add(format, *args)
    this.newLine()
    return this
}

fun CodeBlock.Builder.newLine(): CodeBlock.Builder {
    this.add("\n")
    return this
}

fun CodeBlock.Builder.comment(comment: String): CodeBlock.Builder {
    this.add("// $comment\n")
    return this
}

fun CodeBlock.Builder.addEscaped(format: String): CodeBlock.Builder {
    this.add(format.replace("$", "$$"))
    return this
}

fun CodeBlock.Builder.addEscapedStatement(format: String): CodeBlock.Builder {
    this.addStatement(format.replace("$", "$$"))
    return this
}

fun <T> CodeBlock.Builder.autoControlFlow(controlFlow: String, vararg args: Any, func: CodeBlock.Builder.() -> T): T {
    beginControlFlow(controlFlow, *args)
    val result = func(this)
    endControlFlow()
    return result
}

fun <T> CodeBlock.Builder.`if`(
        condition: String,
        vararg args: Any,
        func: CodeBlock.Builder.() -> T): T = autoControlFlow("if ($condition)", *args, func = func)

fun <T> CodeBlock.Builder.`while`(
        condition: String,
        vararg args: Any,
        func: CodeBlock.Builder.() -> T): T = autoControlFlow("while ($condition)", *args, func = func)

fun <T> CodeBlock.Builder.switch(
        condition: String,
        vararg args: Any,
        func: CodeBlock.Builder.() -> T): T = autoControlFlow("switch ($condition)", *args, func = func)

fun <T> CodeBlock.Builder.`for`(
        condition: String,
        vararg args: Any,
        func: CodeBlock.Builder.() -> T): T = autoControlFlow("for ($condition)", *args, func = func)

fun <T> CodeBlock.Builder.case(
        label: String,
        func: CodeBlock.Builder.() -> T): T {

    addEscaped("case $label:")
    newLine()
    indent()
    val result = func(this)
    addStatement("break")
    unindent()
    newLine()
    return result
}

fun <T> CodeBlock.Builder.default(func: CodeBlock.Builder.() -> T): T {
    add("default:")
    newLine()
    indent()
    val result = func(this)
    addStatement("break")
    unindent()
    newLine()
    return result
}
