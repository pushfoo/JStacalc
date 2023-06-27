package com.github.pushfoo.jstacalc.vm.words.defs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.pushfoo.jstacalc.vm.words.defs.IWordDef.UNDEFINED;

/**
 * <p>Mandatory label to for static methods which are used as built-ins.</p>
 *
 * <p>pops and pushes must be set to either NUM_VARIES or an int value >= 0</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface BuiltIn {
    // nulls are not permitted for default values, so we have to use some dirty tricks
    String name()     default "";

    int    pops()     default UNDEFINED;
    int    pushes()   default UNDEFINED;
    String helpText()  default "";
}
