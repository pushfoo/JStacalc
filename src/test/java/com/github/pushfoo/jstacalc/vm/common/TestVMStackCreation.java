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

    //TODO: fix the underlying logic
    //@ParameterizedTest
    //@MethodSource("sizeAndPaddingArguments")
    void givenStackCreatedWithCopyOf_WhenPaddingSizesNotZero_SameNumberOfNullsAddedToEndOfStack(
        Integer stackSize, Integer paddingSize
    ) {
        List<Integer> l = rangeList(stackSize);
        int sizeWithPadding = stackSize + paddingSize;
        VMStack<Integer> testOperationStack = VMStack.copyOf(l, sizeWithPadding);
        assertIterableEquals(
                Seq.of((Object) null).cycle(paddingSize),
                testOperationStack.stream().skip(stackSize).toList());
    }

    @Test
    void givenCopyOfIsCalled_WhenVMStackIsCopySource_resultingStackEquivalent() {
        List<Integer> sourceList = rangeList(10);
        VMStack<Integer> sourceStack = VMStack.copyOf(sourceList);
        testOperationStack = VMStack.copyOf(sourceStack);
        assertIterableEquals(sourceList, testOperationStack);
    }

}