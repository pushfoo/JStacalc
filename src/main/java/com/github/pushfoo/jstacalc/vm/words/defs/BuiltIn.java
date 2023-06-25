package com.github.pushfoo.jstacalc.vm.words.defs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.pushfoo.jstacalc.vm.words.defs.IWordDef.UNKNOWN;

/**
 * Label a static method or a field as a built-in.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface BuiltIn {
    // nulls are not permitted for default values, so we have to use some dirty tricks
    String name()     default "";

    int    pops()     default UNKNOWN;
    int    pushes()   default UNKNOWN;
    String comment()  default "";
}
