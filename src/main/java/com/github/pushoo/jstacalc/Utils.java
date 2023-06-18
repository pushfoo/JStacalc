package com.github.pushoo.jstacalc;

import java.lang.reflect.Parameter;

public class Utils {

    /**
     * Check if all arguments for a parameter array are of the given type.
     *
     * @param parameters the parameter array to check
     * @param clazz      class to check membership of
     * @return whether all parameters are of the class in clazz
     */
    public static boolean hasOnlyArgsOfType(Parameter[] parameters, Class<?> clazz) {
        if (parameters.length < 1) return false;

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
     * Check if all arguments to a parameter array are ints.
     *
     * @param parameters an array of Parameter to check
     * @return whether all arguments are ints
     */
    public static boolean hasOnlyIntArgs(Parameter[] parameters) {
        return hasOnlyArgsOfType(parameters, int.class);
    }
}
