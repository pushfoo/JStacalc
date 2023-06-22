package com.github.pushfoo.jstacalc.vm.common;

import org.jooq.lambda.Seq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TestVMStackPushing extends BaseVMStackTest.EmptyVMStackTest {

    @Test
    void givenEmptyStack_whenPushingSingle_ItemValueIsPushedOnceOntoRight() {
        testOperationStack.push(10);
        assertIterableEquals(Seq.of(10).toList(), testOperationStack);
    }

    static Stream<Arguments> newPushContents() {
        return Seq.of(
                arguments(0, 0, 3),
                arguments(1, 2, 5),
                arguments(10, 20000, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("newPushContents")
    void givenPushCalledOnStackS_whenArgumentIsArray_arrayValuesArePushedInOrderOntoToRight(
            Integer initialStackSize, Integer arrayStartValue, Integer arrayLength
    ) {
        fillStackWithIndices(testOperationStack, initialStackSize);
        List<Integer> arrayAsList = Seq
                .range(arrayStartValue, arrayStartValue + arrayLength)
                .toList();

        testOperationStack.push(arrayAsList.toArray(Integer[]::new));
        List<Integer> addedPortionOfStack = testOperationStack
                .stream()
                .toList()
                .subList(initialStackSize, initialStackSize + arrayLength);
        assertIterableEquals(arrayAsList, addedPortionOfStack);
    }

    @ParameterizedTest
    @MethodSource("newPushContents")
    void givenPushCalledStack_whenArgumentIsList_StackAppendsElements(
            Integer initialStackSize, Integer listStartValue, Integer listLength
    ) {
        fillStackWithIndices(testOperationStack, initialStackSize);
        List<Integer> addedList = Seq.
                range(listStartValue, listStartValue + listLength)
                .toList();

        testOperationStack.push(addedList);
        List<Integer> addedPortionOfStack = testOperationStack
                .stream()
                .toList()
                .subList(initialStackSize, initialStackSize + listLength);
        assertIterableEquals(addedList, addedPortionOfStack);
    }

    static Stream<Arguments> nonZeroSizeAndPaddingArguments() {
        return nonEmptyListSizes()
                .crossJoin(nonZeroPaddingSizes())
                .map(t -> t.map(Arguments::of));
    }

    @ParameterizedTest
    @MethodSource("nonZeroSizeAndPaddingArguments")
    void givenPadWithWasCalled_withNonZeroSizeAndPadding_stackAppendsRequestedPaddingLength(
            int initialStackSize, int numToPadWith
    ) {
        // Set up & compute expected sequence
        fillStackWithIndices(testOperationStack, initialStackSize);
        List<Integer> expectedResult = rangeSeq(initialStackSize)
                .append(Seq.of(sentinelPadValue).cycle(numToPadWith))
                .toList();

        // Perform operation & check results
        testOperationStack.padWith(numToPadWith, sentinelPadValue);
        assertIterableEquals(expectedResult, testOperationStack);
    }

    @ParameterizedTest
    @MethodSource("nonZeroSizeAndPaddingArguments")
    void givenPadToCalled_withNonZeroSizeAndNumToPadTo_stackAppendsPaddingWhenNumToPadToGreaterThanSize(
            int stackSize, int numToPadTo
    ) {
        int expectedPaddingLength = numToPadTo - stackSize;

        // Build lists
        List<Integer> leftFiller = rangeList(stackSize);
        List<Integer> expectedPadding = repeatNTimesList(
                max(0, expectedPaddingLength), sentinelPadValue);

        List<Integer> expectedFinal = Seq.concat(leftFiller, expectedPadding).toList();

        // Apply operations on the stack
        fillStackWithIndices(testOperationStack, stackSize);
        int reportedPaddingAdded = testOperationStack.padTo(numToPadTo, sentinelPadValue);

        // Check result
        assertEquals(expectedPaddingLength, reportedPaddingAdded);
        assertIterableEquals(expectedFinal, testOperationStack);
    }
}