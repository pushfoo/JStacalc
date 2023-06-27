package com.github.pushfoo.jstacalc.vm.exceptions;


import java.lang.reflect.Member;

public class VMBuiltInAnnotationException extends VMBuiltInDefinitionException {
    public VMBuiltInAnnotationException(String registeredName, Member member, String message) {
        super(registeredName, member, message);
    }

    public VMBuiltInAnnotationException(String registeredName, Member member, Throwable cause) {
        super(registeredName, member, cause);
    }
}
