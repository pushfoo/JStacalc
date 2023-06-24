package com.github.pushfoo.jstacalc.vm;

import com.github.pushfoo.jstacalc.vm.words.defs.NameOverride;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

public class CommonMethodSources {

    /**
     * Converts all static methods on inheriting classes into an Arguments object.
     */
    static abstract class StaticMethodTestArgumentsProvider implements ArgumentsProvider {
         public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws SecurityException {
             // does not use any of the projects static helpers because they're the features which need to be tested
            return Stream.of(this.getClass().getDeclaredMethods())
                    .filter(method -> Modifier.isStatic(method.getModifiers()))
                    .map(method -> Arguments.of((Object) method.getParameters()));
        }
    }

    public static class StaticMethodsWithAllIntParams extends StaticMethodTestArgumentsProvider {

        // Ignore what IntelliJ and other linters say: these are used by reflection
        @NameOverride("one")
        public static void hasOneIntArgs(int a) {
        }

        @NameOverride("two")
        public static void hasTwoIntArgs(int a, int b) {
        }

        @NameOverride("three")
        public static void hasThreeIntArgs(int a, int b, int c) {
        }

        @NameOverride("five")
        public static int hasFiveArgsReturnsInt(int a, int b, int c, int d, int e) {
            return 1;
        }
    }

    public static class StaticMethodsWithMixedParamTypes extends StaticMethodTestArgumentsProvider {

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

    public static class ZeroArgsVoidReturnMethods extends StaticMethodTestArgumentsProvider {
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
