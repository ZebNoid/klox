package org.lox.interpreter

import org.lox.token.Token

class RuntimeError(
    val token: Token,
    message: String
) : RuntimeException(message)