package com.github.pushfoo.jstacalc.vm.exceptions;

import java.lang.reflect.Method;

public class VMBuiltInSignatureException extends VMBuiltInDefinitionException {

    public VMBuiltInSignatureException(String registeredName, Method method, String message) {
        super(registeredName, method, message);
    }

    public VMBuiltInSignatureException(String registeredName, Method method, String message, Throwable cause) {
        super(new ExceptionMessageBuilder().prepend(message), registeredName, method, cause);
    }
}
