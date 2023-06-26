package com.github.pushfoo.jstacalc.vm.exceptions;

import com.github.pushfoo.jstacalc.tokens.FilePosition;
import com.github.pushfoo.jstacalc.tokens.Token;
import lombok.Getter;


public class VMExecutionException extends VMException {
    public final Token token;

    protected VMExecutionException(ExceptionMessageBuilder builder, Token token) {
       super(builder.prependLocation(token));
       this.token = token;
    }
    public VMExecutionException(Token token) {
        this(new ExceptionMessageBuilder(), token);
    }

    public VMExecutionException(Token token, String message, Throwable cause) {
        this(
                new ExceptionMessageBuilder().prependLocation(token).prepend(message),
                token, cause
        );
    }

    protected VMExecutionException(ExceptionMessageBuilder builder, Token token, Throwable cause) {
        super(builder.prependLocation(token), cause);
        this.token = token;
    }

    VMExecutionException(Token token, Throwable cause) {
        this(new ExceptionMessageBuilder(), token, cause);
    }

}
