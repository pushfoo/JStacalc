package com.github.pushfoo.jstacalc;

import com.github.pushfoo.jstacalc.vm.CommonMethodSources;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.reflect.Parameter;

import static com.github.pushfoo.jstacalc.ReflectionHelpers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectionHelpersTest {

    @Test
    void isStaticCalled_whenStaticMethodIsArgument_returnsTrue() throws NoSuchMethodException {
        assertTrue(isStatic(isStaticTestHelper.class.getMethod("staticMethod")));
    }

    @Test
    void isStaticCalled_whenInstanceMethodIsArgument_returnsFalse() throws NoSuchMethodException {
        assertFalse(isStatic(isStaticTestHelper.class.getMethod("instanceMethod")));
    }

    static class isStaticTestHelper {
        public static void staticMethod() {}
        public        void instanceMethod() {}
    }

    @ParameterizedTest
    @ArgumentsSource(CommonMethodSources.StaticMethodsWithAllIntParams.class)
    void hasOnlyArgsOfTypeCalled_whenParametersAllInts_returnsTrue(Parameter[] params) {
        assertTrue(hasOnlyArgsOfType(params, int.class));
    }

    @ParameterizedTest
    @ArgumentsSource(CommonMethodSources.ZeroArgsVoidReturnMethods.class)
    void hasOnlyArgsOfTypeCalled_whenParametersIsEmpty_returnsFalse(Parameter[] parameters) {
        assertFalse(hasOnlyArgsOfType(parameters, int.class));
    }

    @ParameterizedTest
    @ArgumentsSource(CommonMethodSources.StaticMethodsWithMixedParamTypes.class)
    void hasOnlyArgsOfTypeCalled_whenParametersIsOfMultipleTypes_returnsFalse(Parameter[] parameters) {
        assertFalse(hasOnlyArgsOfType(parameters, int.class));
    }

    @ParameterizedTest
    @ArgumentsSource(CommonMethodSources.StaticMethodsWithAllIntParams.class)
    void hasOnlyIntArgsCalled_whenParametersIsAllInts_returnsTrue(Parameter[] parameters) {
        assertTrue(hasOnlyIntArgs(parameters));
    }

    @ParameterizedTest
    @ArgumentsSource(CommonMethodSources.ZeroArgsVoidReturnMethods.class)
    void hasOnlyIntArgsCalled_whenParametersHasZeroArgs_returnsFalse(Parameter[] parameters) {
        assertFalse(hasOnlyIntArgs(parameters));
    }

    @ParameterizedTest
    @ArgumentsSource(CommonMethodSources.StaticMethodsWithMixedParamTypes.class)
    void hasOnlyIntArgsCalled_whenParametersHasMultipleArgTypes_returnsFalse(Parameter[] parameters) {
        assertFalse(hasOnlyIntArgs(parameters));
    }
}