package com.github.pushfoo.jstacalc.vm.common;

import org.jooq.lambda.Seq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestVMStackCreation extends BaseVMStackTest {

    @Test
    void givenStackCreatedDirectlyWithNoArguments_whenIsEmptyIsCalled_trueIsReturned(
    ) {
        testOperationStack = new VMStack<>();
        assertTrue(testOperationStack.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("nonEmptyListSizesArguments")
    void givenStackCreatedWithCopyOf_WhenOnlyAListIsTheArgument_theStackIsIdenticalInValue(
            Integer stackSize
    ) {
        List<Integer> l = rangeList(stackSize);
        VMStack<Integer> stack = VMStack.copyOf(l);
        assertIterableEquals(l, stack);
    }

    static Stream<Arguments> sizeAndPaddingArguments() {
        return nonEmptyListSizes()
                .crossJoin(nonZeroPaddingSizes())
                .map(t -> Arguments.of(t.v1, t.v2));
    }

    @ParameterizedTest
    @MethodSource("sizeAndPaddingArguments")
    void givenStackCreatedWithCopyOf_WhenLengthGreaterThanOriginal_SameNumberOfNullsAddedToEndOfStack(
        Integer initialStackSize, Integer paddingSize
    ) {
        List<Integer> l = rangeList(initialStackSize);
        int sizeWithPadding = initialStackSize + paddingSize;

        VMStack<Integer> testOperationStack = VMStack.copyOf(l, sizeWithPadding);
        assertIterableEquals(
                // Important: Seq.cycle might ignore passed limits under some circumstances!
                // This may be null-specific, and should be investigated at a later time.
                Seq.of((Integer) null).cycle().limit(paddingSize),
                testOperationStack.stream().skip(initialStackSize).toList());
    }

    @Test
    void givenCopyOfIsCalled_WhenVMStackIsCopySource_resultingStackEquivalent() {
        List<Integer> sourceList = rangeList(10);
        VMStack<Integer> sourceStack = VMStack.copyOf(sourceList);
        testOperationStack = VMStack.copyOf(sourceStack);
        assertIterableEquals(sourceList, testOperationStack);
    }

}