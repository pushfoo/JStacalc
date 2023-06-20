package com.github.pushfoo.jstacalc.vm.exceptions;

import com.github.pushfoo.jstacalc.tokens.Token;

public class VMExecutionException extends VMException {
    private final Token token;
    public Token token() { return token; }

    public VMExecutionException(Token token, String message, Throwable cause) {
        super(message, cause);
        this.token = token;
    }

    public VMExecutionException(Token token) {
        super();
        this.token = token;
    }

    VMExecutionException(Token token, Throwable cause) {
        super(cause);
        this.token = token;
    }
}
