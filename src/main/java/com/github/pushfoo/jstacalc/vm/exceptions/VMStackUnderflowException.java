package com.github.pushfoo.jstacalc.vm.exceptions;

import com.github.pushfoo.jstacalc.tokens.Token;

/**
 * Signals too few operands on the stack for a given word.
 */
public class VMStackUnderflowException extends VMExecutionException {

    public VMStackUnderflowException(Token token) {
        super(token);
    }
    public VMStackUnderflowException(Token token, Throwable cause) {
        super(token, cause);
    }
}

