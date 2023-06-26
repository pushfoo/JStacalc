package com.github.pushfoo.jstacalc.vm.exceptions;

import com.github.pushfoo.jstacalc.tokens.Token;

import static java.util.Objects.nonNull;

public class VMException extends Exception {

    public VMException() {
        super();
    }

    public VMException(String message) {
        super(message);
    }

    /**
     * Finalize the exception message before we ascend the class hierarchy to finish initialization.
     *
     * @param builder the message builder.
     */
    protected VMException(ExceptionMessageBuilder builder) {
        this(builder.toString());
    }

    public VMException(String message, Throwable cause) {
        super(message, cause);
    }

    public VMException(Throwable cause) {
        super(cause);
    }

    protected VMException(ExceptionMessageBuilder builder, Throwable cause) {
        this(builder.toString(), cause);
    }
}
