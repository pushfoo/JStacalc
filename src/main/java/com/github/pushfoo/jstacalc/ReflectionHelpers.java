package com.github.pushfoo.jstacalc;

import java.lang.reflect.Parameter;

public class ReflectionHelpers {

    /**
     * Return true if all present args are of the passed type.
     *
     * @param parameters The parameters argument extracted from a method.
     * @param clazz      The class to match.
     * @return whether all present arguments to the method are of the requested type.
     */
    public static boolean allArgsOfType(Parameter[] parameters, Class<?> clazz) {
        boolean allOfType = true;
        for (Parameter parameter : parameters) {
            // TODO: look for better subclass checking technique
            // not actually always true as IntelliJ may suggest
            allOfType &= parameter.getType().equals(clazz);
            if ( ! allOfType ) break;
        }
        return allOfType;
    }

    /**
     * Check if all present arguments are ints
     *
     * @param parameters A parameters object returned from a Method reflection.
     * @return whether all present parameters are ints
     */
    public static boolean allArgsAreInts(Parameter[] parameters) {
        return allArgsOfType(parameters, int.class);
    }

    /**
     * Check if n > 0 parameters are present & all are of the specified type.
     *
     * @param parameters the parameter array to check
     * @param clazz      class to check membership of
     * @return whether the method takes n > 0 arguments of type clazz
     */
    public static boolean hasOnlyArgsOfType(Parameter[] parameters, Class<?> clazz) {
        return (parameters.length > 0) && allArgsOfType(parameters, clazz);
    }

    /**
     * Check if n > 0 parameters are present & all are ints.
     *
     * @param parameters an array of Parameter to check
     * @return whether the method takes n > 0 ints as arguments
     */
    public static boolean hasOnlyIntArgs(Parameter[] parameters) {
        return hasOnlyArgsOfType(parameters, int.class);
    }
}
