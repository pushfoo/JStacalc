package com.github.pushfoo.jstacalc.vm.words.builtins;

import com.github.pushfoo.jstacalc.Helpers;
import com.github.pushfoo.jstacalc.vm.VMCore;
import org.jooq.lambda.Seq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArithmeticTest {

    VMCore core;

    @BeforeEach
    void setup() {
        core = new VMCore();
    }

    /**
     * Add swapped versions of a & b to a stream when a != b, leaving the result in the same place.
     *
     * @param entry A list to map to its commutative reverse ordering
     * @return Stream.of(List.of(a, b, result)) if a != b, else Stream.of(List.of(a, b, result), List.of(b, a, result))
     */
    static <E> Stream<List<E>> withCommutativeOrderVariants(List<E> entry) {
        E a = entry.get(0);
        E b = entry.get(1);

        return a.equals(b) ?
                  Stream.of(entry)
                : Stream.of(entry, List.of(b, a, entry.get(2)));
    }

    public static Seq<List<? extends Integer>> withSignVariants(List<Integer> unsigned) {
        int a = unsigned.get(0);
        int b = unsigned.get(1);
        int expected = unsigned.get(2);

        Seq<List<? extends Integer>> result = Seq.of(unsigned);
        if (a != 0          ) result = result.concat(Seq.of(List.of( -a,  b, -expected)));
        if (b != 0          ) result = result.concat(Seq.of(List.of(  a, -b, -expected)));
        if (a != 0 && b != 0) result = result.concat(Seq.of(List.of( -a, -b,  expected)));

        return result;
    }

    abstract class AbstractOpTest {
        abstract void operationWrapper();
        void performWrappedOperation(Integer a, Integer b) {
            core.push(a, b);
            operationWrapper();
        }

        static Stream<Integer> sharedSource() {
            return Seq.of(1, 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        static Stream<List<Integer>> partialLeftHandVaryingIdentitySource() {
            return sharedSource()
                .map((Integer i) -> List.of(i, 0, i));
        }

    }

    @Nested
    class AddTest extends AbstractOpTest {

        @Override
        void operationWrapper() {
            Arithmetic.add(core);
        }
        static Stream<Arguments> identitySource() {
            return partialLeftHandVaryingIdentitySource()
                    .flatMap(ArithmeticTest::withCommutativeOrderVariants)
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("identitySource")
        void givenAddCalled_withOneEqualsZero_resultIsNonZeroSide(
                Integer a, Integer b, Integer expectedResult
        ) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }

        static Stream<Arguments> edgeCaseSource() {
            return Stream.of(
                           // Intentional overflows
                            List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE + Integer.MAX_VALUE),
                            List.of(Integer.MAX_VALUE,  1, Integer.MAX_VALUE +  1),
                            List.of(Integer.MIN_VALUE, -1, Integer.MIN_VALUE + -1)
                    )
                    .flatMap(ArithmeticTest::withCommutativeOrderVariants)
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("edgeCaseSource")
        void givenAddCalled_withEdgeCaseArgument_resultEqualsExpected(
                Integer a, Integer b, Integer expectedResult
        ) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.peek());
        }
    }

    @Nested
    class MulTest extends AbstractOpTest {
        void operationWrapper() {
            Arithmetic.mul(core);
        }
        static Stream<Arguments> identitySource() {
            return sharedSource()
                    .map(i -> List.of(i, 1, i))
                    .flatMap(ArithmeticTest::withCommutativeOrderVariants)
                    .map(Helpers::listToArguments);
        }
        @ParameterizedTest
        @MethodSource("identitySource")
        void givenMulCalled_whenAOrBIsOne_resultIsOtherArg(
               Integer a, Integer b, Integer expectedResult
        ) {
           performWrappedOperation(a ,b);
           assertEquals(expectedResult, core.pop());
        }

        static Stream<Arguments> signInversionSource() {
            return sharedSource()
                    .map(i -> List.of(i, -1, -i))
                    .flatMap(ArithmeticTest::withCommutativeOrderVariants)
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("signInversionSource")
        void givenMulCalled_whenAOrBIsNegativeOne_resultIsSignFlippedVersionOfOtherArg(
               Integer a, Integer b, Integer expectedResult
        ) {
           performWrappedOperation(a ,b);
           assertEquals(expectedResult, core.pop());
        }

        static Stream<Arguments> oneOrMoreZeroSource() {
            return sharedSource()
                    .map(i -> List.of(0, i, 0))
                    .flatMap(ArithmeticTest::withSignVariants)
                    .flatMap(ArithmeticTest::withCommutativeOrderVariants)
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("oneOrMoreZeroSource")
        void givenMulCalled_whenAtLeastOneElementIsZero_resultIsZeroRegardlessOfOtherElement(
                Integer a, Integer b
        ) {
            performWrappedOperation(a, b);
            assertEquals(0, core.pop());
        }
        static Stream<Arguments> nonCommutativeOverflowEdgeCaseSource() {
            return Seq.of(
                Arguments.of(
                        Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE * Integer.MIN_VALUE),
                Arguments.of(
                        Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE * Integer.MAX_VALUE)
            );
        }

        @ParameterizedTest
        @MethodSource("nonCommutativeOverflowEdgeCaseSource")
        void givenMulCalled_whenNonCommutativeCase_resultIsEqualExpected(
                Integer a, Integer b, Integer expectedResult
        ) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }

    }

    @Nested
    class SubTest extends AbstractOpTest {
        void operationWrapper() {
           Arithmetic.sub(core);
        }

        static Stream<Arguments> aEqualsBSource() {
            return  sharedSource()
                    .map(i -> List.of(i, i, 0))
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("aEqualsBSource")
        void givenSubCalled_whenAEqualsB_result(Integer a, Integer b, Integer expectedResult) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }

        static Stream<Arguments> identitySource() {
            return sharedSource()
                    .map(i -> List.of(i, 0))
                    .map(Helpers::listToArguments);
        }

        static Stream<Arguments> aEqualsZeroSource() {
            return sharedSource()
                    .map(i -> List.of(0, i, 0 - i))
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("aEqualsZeroSource")
        void givenSubCalled_whenAEqualsZero_resultEqualsZeroMinusB(Integer a, Integer b, Integer expectedResult) {
            // Due to overflows, this isn't always -b
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }

        @ParameterizedTest
        @MethodSource("identitySource")
        void givenSubCalled_whenBEqualsZero_resultEqualsA(Integer a, Integer b) {
            performWrappedOperation(a, b);
            assertEquals(a, core.pop());
        }

        static Stream<Arguments> edgeCaseSource() {
            return Stream.of(
                // Intentional overflows below
                List.of(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE - Integer.MIN_VALUE),
                List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE - Integer.MAX_VALUE)
            ).map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("edgeCaseSource")
        void givenSubCalled_whenEdgeDataProvided_resultEqualsExpectedEdgeCaseResult(Integer a, Integer b, Integer expectedResult) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }
    }


    abstract class AbstractDivModTest extends AbstractOpTest {

        static Seq<Integer> sharedDivModSeq() {
            // do not use 1 or 2 as values here, use it as part of an initial sequence
            // which is concatenated with / appended to.
            return Seq.of(3, 53, 10000);
        }

        static Stream<Arguments> divByZeroSource() {
            return Seq.of(0, 1)
                    .append(sharedDivModSeq())
                    .map(Arguments::of);
        }

        static Stream<Arguments> aEqualsZeroSource() {
            return Seq.concat(
                            Seq.of(1, 2),
                            sharedDivModSeq()
                    )
                    .flatMap(i -> Seq.of(i, -i))
                    .map(i -> List.of(0, i, 0))
                    .map(Helpers::listToArguments);
        }
    }

    @Nested
    class DivTest extends AbstractDivModTest {

        void operationWrapper() {
            Arithmetic.div(core);
        }

        @ParameterizedTest
        @MethodSource("divByZeroSource")
        void givenDivCalled_whenBEqualsZero_throwsArithmeticException(int a){
            core.push(a, 0);
            assertThrows(ArithmeticException.class,() -> Arithmetic.div(core));
        }

        @ParameterizedTest
        @MethodSource("divByZeroSource")
        void givenDivCalled_whenBEqualsZero_leavesStackIntact(int a){
            // We don't use doOp here because we want to be sure exceptions are only swallowed in the right place
            core.push(a, 0);
            try { Arithmetic.div(core); }
            catch (ArithmeticException ignored) {}
            assertEquals(0, core.pop());
            assertEquals(a, core.pop());
        }

        @ParameterizedTest
        @MethodSource("aEqualsZeroSource")
        void givenDivCalled_whenAEqualsZeroButNotB_pushesZeroOntoStack(
                Integer a, Integer b, Integer expectedResult
        ) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }

        static Stream<Arguments> bEquals1Source() {
            return Seq.concat(
                            Seq.of(2),
                            sharedDivModSeq()
                    )
                    .flatMap(i -> Seq.of(i, -i))
                    .map(i -> List.of(i, 1, i))
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("bEquals1Source")
        void givenDivCalled_whenBEqualsOne_pushesOneOntoStack(
                Integer a, Integer b, Integer expectedResult
        ) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }

        static Stream<Arguments> aSameMagnitudeAsBSource() {
            return sharedDivModSeq()
                    .map(i -> List.of(i, i, 1))
                    .flatMap(ArithmeticTest::withSignVariants)
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("aSameMagnitudeAsBSource")
        void givenDivCalled_whenASameMagnitudeAsB_pushes1MagnitudeValueWithAppropriateSignOntoStack(
                Integer a, Integer b, Integer expectedResult
        ) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }

        static Stream<Arguments> integerRoundDownSource() {
            return Seq.concat(
                    Seq.of(List.of(1, 2, 0)),
                    sharedDivModSeq()
                        .flatMap(i -> Seq.of(
                                // Larger divisor rounded down
                                List.of( i        ,  i + 1,  0 ),
                                // Remainders rounded away
                                List.of( 2 * i + 1,      i,  2 )
                            ))
                    )
                    .flatMap(ArithmeticTest::withSignVariants)
                    .map(Helpers::listToArguments);
        }

        @ParameterizedTest
        @MethodSource("integerRoundDownSource")
        void givenDivCalled_whenFractionalResult_resultIsRoundedDown(
                Integer a, Integer b, Integer expectedResult
        ) {
            performWrappedOperation(a, b);
            assertEquals(expectedResult, core.pop());
        }

    }

}