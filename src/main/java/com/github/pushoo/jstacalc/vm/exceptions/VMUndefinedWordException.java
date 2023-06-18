package com.github.pushoo.jstacalc.vm.exceptions;

import com.github.pushoo.jstacalc.tokens.Token;

/**
 * A definition for the given token was not found.
 */
public class VMUndefinedWordException extends VMExecutionException {
    public VMUndefinedWordException(Token token) {
        super(token);
    }
    public VMUndefinedWordException(Token token, Throwable cause) {
        super(token, cause);
    }
}
