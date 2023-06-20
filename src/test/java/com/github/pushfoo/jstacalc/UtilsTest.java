package com.github.pushfoo.jstacalc;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static com.github.pushfoo.jstacalc.Utils.hasOnlyArgsOfType;
import static com.github.pushfoo.jstacalc.Utils.hasOnlyIntArgs;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {
    @Test
    void hasOnlyArgsOfTypeWorksWithInt() {
        for (Method m : AllIntArgsMethods.declaredMethods) {
            assertTrue(hasOnlyArgsOfType(m.getParameters(), int.class));
        }
    }

    @Test
    void hasOnlyArgsOfTypeReturnsFalseWhenZeroArgs() {
        for (Method m : ZeroArgsVoidReturnMethods.declaredMethods) {
            assertFalse(hasOnlyArgsOfType(m.getParameters(), int.class));
        }
    }

    @Test
    void hasOnlyArgsOfTypeReturnsFalseWhenMixedArgs() {
        for (Method m : MixedTypesArgsVoidReturnMethods.declaredMethods) {
            assertFalse(hasOnlyArgsOfType(m.getParameters(), int.class));
        }
    }

    @Test
    void hasOnlyIntArgsReturnsTrueForIntArgs() {
        for (Method m : AllIntArgsMethods.declaredMethods) {
            assertTrue(hasOnlyIntArgs(m.getParameters()));
        }
    }

    @Test
    void hasOnlyIntReturnsFalseWhenZeroArgs() {
        for (Method m : ZeroArgsVoidReturnMethods.declaredMethods) {
            assertFalse(hasOnlyIntArgs(m.getParameters()));
        }
    }

    @Test
    void hasOnlyIntReturnsFalseWhenMixedArgs() {
        for (Method m : MixedTypesArgsVoidReturnMethods.declaredMethods) {
            assertFalse(hasOnlyIntArgs(m.getParameters()));
        }
    }

    static class AllIntArgsMethods {
        final public static Method[] declaredMethods = AllIntArgsMethods.class.getDeclaredMethods();

        // Ignore what IntelliJ and other linters say: these are used further down by reflection
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

    static class MixedTypesArgsVoidReturnMethods {
        final public static Method[] declaredMethods = MixedTypesArgsVoidReturnMethods.class.getDeclaredMethods();

        // Ignore what IntelliJ and other linters say: these are used further down by reflection
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

    static class ZeroArgsVoidReturnMethods {
        final public static Method[] declaredMethods = ZeroArgsVoidReturnMethods.class.getDeclaredMethods();

        // Ignore what IntelliJ and other linters say: these are used further down by reflection
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