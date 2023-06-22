package com.github.pushfoo.jstacalc.vm.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TestVMStackPopping extends BaseVMStackTest.EmptyVMStackTest {

    static Stream<Arguments> stackSizeGreaterOrEqualToNumItems() {
        return Stream.of(
                arguments(1, 1),
                arguments(10, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("stackSizeGreaterOrEqualToNumItems")
    void givenStackHasEntries_whenPoppingWithNoArgs_OneElementPopped(
            Integer stackSize, Integer numItems
    ) {
        fillStackWithIndices(testOperationStack, stackSize);
        testOperationStack.pop();
        assertIterableEquals(
                rangeList(stackSize - 1),
                testOperationStack);
    }

    @ParameterizedTest
    @MethodSource("stackSizeGreaterOrEqualToNumItems")
    void givenStackHasEntries_WhenPoppingWithNumItemsLessThanOrEqualToSize_PopsRequestedAmount(
            Integer stackSize, Integer numItems
    ) {

        fillStackWithIndices(testOperationStack, stackSize);
        testOperationStack.pop(numItems);
        assertIterableEquals(
                rangeList(stackSize - numItems),
                testOperationStack);
    }
}