package com.github.pushfoo.jstacalc;

import org.junit.jupiter.params.provider.Arguments;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

// TODO: look into replacing this with something more idiomatic to JUnit 5 / recent Java
/**
 * Static helper methods for tests.
 */
public class Helpers {

    /**
     * Extract static methods which match a predicate as arguments from the passed classes.
     *
     * @param filterPredicate filter the returned
     * @param transformation  a converter function to turn the method into arguments
     * @param classes         classes to extract methods from
     * @return a stream of arguments returnable as a static method source
     */
    public static Stream<? extends Arguments> staticMethodsAsArgumentsStream(
        Predicate<Method> filterPredicate,
        Function<? super Method, ? extends Arguments> transformation,
        Class<?>... classes) {
        return Stream.of(classes)
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(filterPredicate)
                .map(transformation);
    }
    
    /**
     * Extract static methods which match a predicate as arguments from the passed classes.
     *
     * @param filterPredicate filter the returned
     * @param classes         classes to extract methods from
     * @return a stream of arguments returnable as a static method source
     */
    public static Stream<? extends Arguments> staticMethodsAsArgumentsStream(
            Predicate<Method> filterPredicate,
            Class<?>... classes
    ) {
        return staticMethodsAsArgumentsStream(
                filterPredicate,
                Arguments::of,
                classes);
    }

    /**
     * Extract all static methods from the passed classes and return them as arguments.
     *
     * @param classes to extract methods from
     * @return a stream of arguments returnable as a static method source
     */
    public static Stream<? extends Arguments> staticMethodsAsArgumentsStream(Class<?>... classes) {
        return staticMethodsAsArgumentsStream(i -> true, classes);
    }
}

