package com.github.pushoo.jstacalc.vm.exceptions;

public class VMException extends Exception {

    public VMException() {
        super();
    }
    public VMException(String message) {
        super(message);
    }
    public VMException(String message, Throwable cause) {
        super(message, cause);
    }
    public VMException(Throwable cause) {
        super(cause);
    }

}
