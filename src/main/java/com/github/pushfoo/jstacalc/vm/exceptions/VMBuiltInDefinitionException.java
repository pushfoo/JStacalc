package com.github.pushfoo.jstacalc.vm.exceptions;

import java.lang.reflect.Member;

import static com.github.pushfoo.jstacalc.vm.exceptions.ExceptionMessageBuilder.singlequote;

public class VMBuiltInDefinitionException extends VMException {
    public final String registeredName;
    public final Member member;

    public static ExceptionMessageBuilder failToDefineBuiltIn(
            ExceptionMessageBuilder builder, String registeredName, Member member) {
        return builder.prepend(
                "Failed to define built-in word ", singlequote(registeredName), " from ",
                member.getDeclaringClass(), ".", member.getName());
    }

    public static ExceptionMessageBuilder failToDefineBuiltIn(String registeredName, Member member) {
        return failToDefineBuiltIn(new ExceptionMessageBuilder(), registeredName, member);
    }

    public VMBuiltInDefinitionException(String registeredName, Member member) {
        this(new ExceptionMessageBuilder(), registeredName, member);
    }

    protected VMBuiltInDefinitionException(ExceptionMessageBuilder builder, String registeredName, Member member) {
        super(failToDefineBuiltIn(builder, registeredName, member));
        this.registeredName = registeredName;
        this.member = member;
    }

    public VMBuiltInDefinitionException(String registeredName, Member member, String message) {
        this(new ExceptionMessageBuilder().prepend(message), registeredName, member);
    }

    protected VMBuiltInDefinitionException(ExceptionMessageBuilder builder, String registeredName, Member member, Throwable cause) {
        super(builder, cause);
        this.registeredName = registeredName;
        this.member = member;
    }
    public VMBuiltInDefinitionException(String registeredName, Member member, Throwable cause) {
        this(new ExceptionMessageBuilder(), registeredName, member, cause);
    }

    public VMBuiltInDefinitionException(String registeredName, Member member, String message, Throwable cause) {
        this(failToDefineBuiltIn(registeredName, member).prepend(message), registeredName, member, cause);
    }

}
