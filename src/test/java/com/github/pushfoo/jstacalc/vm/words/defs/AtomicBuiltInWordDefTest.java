package com.github.pushfoo.jstacalc.vm.words.defs;

import com.github.pushfoo.jstacalc.Helpers;
import com.github.pushfoo.jstacalc.vm.VMCore;
import com.github.pushfoo.jstacalc.vm.exceptions.VMBuiltInAnnotationException;
import com.github.pushfoo.jstacalc.vm.exceptions.VMBuiltInSignatureException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AtomicBuiltInWordDefTest {

    public static Stream<? extends Arguments> failsMethodSignatureValidationSource() {
        return Helpers.staticMethodsAsArgumentsStream(
                PassAnnotationValidationFailSignatureValidation.class
        );
    }

    static Stream<? extends Arguments> passesAllValidationSource() {
        return Helpers.staticMethodsAsArgumentsStream(PassAnnotationValidationPassSignatureValidation.class);
    }

    static Stream<? extends Arguments> methodsHaveNoAnnotationNameSource() {
        return Helpers.staticMethodsAsArgumentsStream(
                Predicates.hasBuiltInAnnotationWithoutName,
                (Method m) -> Arguments.of(m, m.getName()),
                PassAnnotationValidationPassSignatureValidation.class,
                PassAnnotationValidationFailSignatureValidation.class
        );
    }

    static Stream<? extends Arguments> methodsHaveNameInBuiltInAnnotationSource() {
        return Helpers.staticMethodsAsArgumentsStream(
                Predicates.hasBuiltInAnnotationWithName,
                (Method m) -> Arguments.of(m, m.getAnnotation(BuiltIn.class).name()),
                PassAnnotationValidationFailSignatureValidation.class,
                PassAnnotationValidationPassSignatureValidation.class
        );
    }

    static Stream<? extends Arguments> failsAnnotationValidationSource() {
        return Helpers.staticMethodsAsArgumentsStream(FailAnnotationValidationPassSignatureValidation.class);
    }

    @ParameterizedTest
    @MethodSource("failsMethodSignatureValidationSource")
    void givenValidateMethodSignatureCalled_whenArgumentIsBadSignature_returnsFalse(Method method) {
        assertFalse(AtomicBuiltInWordDef.isValidMethodSignature(method));
    }

    @ParameterizedTest
    @MethodSource("passesAllValidationSource")
    void givenValidateMethodSignatureCalled_whenArgumentIsValidMethod_returnTrue(Method method) {
        assertTrue(AtomicBuiltInWordDef.isValidMethodSignature(method));
    }

    @ParameterizedTest
    @MethodSource("passesAllValidationSource")
    void givenProcessAnnotationCalled_withValidAnnotations_returnsTrue(Method method) {
        AtomicBuiltInWordDef.AtomicBuiltInWordDefBuilder<?, ?> builder = AtomicBuiltInWordDef.builder();
        assertDoesNotThrow(() -> {
            AtomicBuiltInWordDef.processAnnotation(builder, method);
        });
    }

    @ParameterizedTest
    @MethodSource("methodsHaveNoAnnotationNameSource")
    void givenProcessAnnotationCalled_withMethodWithoutNameInAnnotation_usesOriginalName(
            Method method, String originalMethodName
    ) throws VMBuiltInAnnotationException {
        AtomicBuiltInWordDef.AtomicBuiltInWordDefBuilder<?, ?> builder = AtomicBuiltInWordDef.builder();
        AtomicBuiltInWordDef.processAnnotation(builder, method);

        // Test result
        AtomicBuiltInWordDef def = builder.build();
        assertEquals(originalMethodName, def.getName());
    }

    @ParameterizedTest
    @MethodSource("methodsHaveNameInBuiltInAnnotationSource")
    void givenProcessAnnotationCalled_withMethodWithNameSetInAnnotation_usesNewName(
            Method method, String newName
    ) throws VMBuiltInAnnotationException {
        AtomicBuiltInWordDef.AtomicBuiltInWordDefBuilder<?, ?> builder = AtomicBuiltInWordDef.builder();
        AtomicBuiltInWordDef.processAnnotation(builder, method);

        // Test result
        AtomicBuiltInWordDef def = builder.build();
        assertEquals(newName, def.getName());
    }

    @ParameterizedTest
    @MethodSource("failsAnnotationValidationSource")
    void givenProcessAnnotationCalled_whenMethodAnnotationInvalid_throwsIllegalArgumentException(
            Method method
    ) {
        assertThrows(VMBuiltInAnnotationException.class, () -> {
            AtomicBuiltInWordDef.processAnnotation(AtomicBuiltInWordDef.builder(), method);
        });
    }

    @ParameterizedTest
    @MethodSource("failsAnnotationValidationSource")
    void givenFromAnnotatedStaticMethodCalled_whenPassedMethodHasBadAnnotation_throwsVMBuiltInAnnotationException(
            Method method
    ) {
        assertThrows(VMBuiltInAnnotationException.class, () -> {
            AtomicBuiltInWordDef.fromAnnotatedStaticMethod(method);
        });
    }

    @ParameterizedTest
    @MethodSource("failsMethodSignatureValidationSource")
    void givenFromAnnotatedStaticMethodCalled_whenPassedMethodHasBadSignature_throwsVMBuiltInSignatureException(
            Method method
    ) {
        assertThrows(VMBuiltInSignatureException.class, () -> {
            AtomicBuiltInWordDef.fromAnnotatedStaticMethod(method);
        });
    }

    static abstract class PassAnnotationValidationFailSignatureValidation {

        @BuiltIn(name = "notPublic", pops = 0, pushes = 0)
        private static void notDeclaredAsPublic(VMCore core) {
        }

        @BuiltIn(pops = 0, name = "intInsteadOfVoid", pushes = 1)
        public static int hasIntReturnTypeInsteadOfVoid(VMCore core) {
            return 1;
        }

        @BuiltIn(pops = 0, pushes = 0)
        public static void wrongNumArgumentsButCorrectType(VMCore core, VMCore core2) {
        }

        @BuiltIn(pops = 0, pushes = 0)
        public static void wrongArgumentTypeButCorrectQuantity(int arg) {
        }

        @BuiltIn(name = "everythingWrong", pops = 0, pushes = 0)
        private void everythingIsWrongOnThisMethodSignature(int arg, String another) {
        }

        @BuiltIn(name = "notStatic", pops = 0, pushes = 0)
        public void notDeclaredAsStatic(VMCore core) {
        }

        @BuiltIn(name = "abstractCantBeStatic", pops = 0, pushes = 0)
        public abstract void notStaticAbstract(VMCore core);
    }

    static abstract class PassAnnotationValidationPassSignatureValidation {

        @BuiltIn(pushes = 0, pops = 0, helpText = "a")
        public static void reversedPushesPopsOrder(VMCore core) {
        }

        @BuiltIn(name = "nop", pops = 1, pushes = 1, helpText = "( a -- a )")
        public static void popAndPutBack(VMCore core) {
            core.push(core.pop());
        }
    }

    static abstract class FailAnnotationValidationPassSignatureValidation {
        @BuiltIn(name = "setsOnlyName")
        public static void setsOnlyNameString(VMCore core) {
        }

        @BuiltIn(helpText = "()")
        public static void setsHelpButNotPushesOrPops(VMCore core) {
        }

        @BuiltIn(pushes = 0)
        public static void onlySetsPushes(VMCore core) {
        }

        @BuiltIn(pops = 0)
        public static void onlySetsPops(VMCore core) {
        }
    }

}