package com.github.pushfoo.jstacalc.vm.words.defs;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

public class Predicates {
    public final static Predicate<Method> hasBuiltInAnnotation = (Method m) ->
            nonNull(m.getAnnotation(BuiltIn.class));
    public final static Predicate<Method> builtInAnnotationHasEmptyName = (Method m) ->
            m.getAnnotation(BuiltIn.class).name().equals("");
    public final static Predicate<Method> hasBuiltInAnnotationWithoutName =
            hasBuiltInAnnotation.and(builtInAnnotationHasEmptyName);
    public final static Predicate<Method> hasBuiltInAnnotationWithName =
            hasBuiltInAnnotation.and(builtInAnnotationHasEmptyName.negate());
}
