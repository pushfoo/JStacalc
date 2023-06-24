package com.github.pushfoo.jstacalc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

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
    @ArgumentsSource(AllIntArgsMethods.class)
    void hasOnlyArgsOfTypeCalled_whenParametersAllInts_returnsTrue(Parameter[] params) {
        assertTrue(hasOnlyArgsOfType(params, int.class));
    }

    @ParameterizedTest
    @ArgumentsSource(ZeroArgsVoidReturnMethods.class)
    void hasOnlyArgsOfTypeCalled_whenParametersIsEmpty_returnsFalse(Parameter[] parameters) {
        assertFalse(hasOnlyArgsOfType(parameters, int.class));
    }

    @ParameterizedTest
    @ArgumentsSource(MixedTypesArgsVoidReturnMethods.class)
    void hasOnlyArgsOfTypeCalled_whenParametersIsOfMultipleTypes_returnsFalse(Parameter[] parameters) {
        assertFalse(hasOnlyArgsOfType(parameters, int.class));
    }

    @ParameterizedTest
    @ArgumentsSource(AllIntArgsMethods.class)
    void hasOnlyIntArgsCalled_whenParametersIsAllInts_returnsTrue(Parameter[] parameters) {
        assertTrue(hasOnlyIntArgs(parameters));
    }

    @ParameterizedTest
    @ArgumentsSource(ZeroArgsVoidReturnMethods.class)
    void hasOnlyIntArgsCalled_whenParametersHasZeroArgs_returnsFalse(Parameter[] parameters) {
        assertFalse(hasOnlyIntArgs(parameters));
    }

    @ParameterizedTest
    @ArgumentsSource(MixedTypesArgsVoidReturnMethods.class)
    void hasOnlyIntArgsCalled_whenParametersHasMultipleArgTypes_returnsFalse(Parameter[] parameters) {
        assertFalse(hasOnlyIntArgs(parameters));
    }

    /**
     * Converts all static methods on inheriting classes into an Arguments object.
     */
    static abstract class StaticMethodUtility implements ArgumentsProvider {
         public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws SecurityException {
            return Stream.of(this.getClass().getDeclaredMethods())
                    .filter(method -> Modifier.isStatic(method.getModifiers()))
                    .map(method -> Arguments.of((Object) method.getParameters()));
        }

    }
    public static class AllIntArgsMethods extends StaticMethodUtility {

        // Ignore what IntelliJ and other linters say: these are used by reflection
        public static void hasOneIntArgs(int a) {
        }

        public static void hasTwoIntArgs(int a, int b) {
        }

        public static void hasThreeIntArgs(int a, int b, int c) {
        }

        public static int hasFiveArgsReturnsInt(int a, int b, int c, int d, int e) {
            return 1;
        }
    }

    static class MixedTypesArgsVoidReturnMethods extends StaticMethodUtility {
        final public static Method[] declaredMethods = MixedTypesArgsVoidReturnMethods.class.getDeclaredMethods();

        // Ignore what IntelliJ and other linters say: these are used by reflection
        public static void stringArg(String s) {
            System.out.println(s);
        }

        public static void intStringArgs(int a, String s) {
            System.out.printf("%d %s\n", a, s);
        }

        public static void stringIntIntArgs(String s, int a, int b) {
            System.out.printf("%s %d %d\n", s, a, b);
        }
    }

    static class ZeroArgsVoidReturnMethods extends StaticMethodUtility {
        final public static Method[] declaredMethods = ZeroArgsVoidReturnMethods.class.getDeclaredMethods();

        // Ignore what IntelliJ and other linters say: these are used by reflection
        public static void zeroArgs() {
        }

        public static int zeroArgsReturnsInt() {
            return 0;
        }

        public static boolean zeroArgsReturnsBoolean() {
            return true;
        }
    }
}