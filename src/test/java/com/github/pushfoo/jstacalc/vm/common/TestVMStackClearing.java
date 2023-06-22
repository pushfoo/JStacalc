package com.github.pushfoo.jstacalc.vm.common;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


public class TestVMStackClearing extends BaseVMStackTest.EmptyVMStackTest {

    @ParameterizedTest
    @ArgumentsSource(ValidAmountProvider.class)
    void givenStackWithItems_whenClearingWithNoArguments_clearEmptiesStack(Integer stackSize) {
        fillStackWithIndices(testOperationStack, stackSize);
        testOperationStack.clear();
        // The comparison to 0 is intentional and part of the test
        assertEquals(0, testOperationStack.size());
    }

    @ParameterizedTest
    @ValueSource(ints={1, 5, 10})
    void givenStackWithItems_whenNumItemsEqualToSize_clearingRemovesAllElements(Integer stackSize) {
        fillStackWithIndices(testOperationStack, stackSize);
        testOperationStack.clear(stackSize);
        assertEquals(0, testOperationStack.size());
    }

    @ParameterizedTest
    @ArgumentsSource(ValidAmountProvider.class)
    void givenStackWithItems_whenNumItemsLessThanOrEqualToSize_clearingRemovesThatManyItemsFromHighSide(
            Integer stackSize, int numItems
    ) {
        fillStackWithIndices(testOperationStack, stackSize);
        testOperationStack.clear(numItems);
        assertEquals(stackSize - numItems, testOperationStack.size());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidNumItemsProvider.class)
    void givenStackWithItems_whenNumItemsNegativeOrGreaterThanSize_clearingRaisesIndexOutOfBounds(
            Integer stackSize, Integer badNumItems
    ) {
        fillStackWithIndices(testOperationStack, stackSize);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            testOperationStack.clear(badNumItems);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidNumItemsProvider.class)
    void testInvalidArgumentsDoNotAlterStack(Integer stackSize, Integer badNumItems) {
        fillStackWithIndices(testOperationStack, stackSize);
        try {
            testOperationStack.clear(badNumItems);
        } catch (IndexOutOfBoundsException ignored) {

        }
        assertIterableEquals(rangeList(stackSize), testOperationStack);
    }

    static abstract class StackAndClearArgProvider implements ArgumentsProvider {

        public static Seq<Arguments> generateSingleBoundValues(
                Integer[] sizes,
                Integer[] offsets,
                Function<Tuple2<Integer, Integer>, Integer> indexFunction) {
            return Seq.of(sizes).crossJoin(Seq.of(offsets)).map(t -> Arguments.of(t.v1, indexFunction.apply(t)));
        }

        // Override this to provide specific pairs of sizes + indices
        public Integer[][] getPresetSizeIndexPairs() {
            return new Integer[][]{};
        }

        // The following 3 items control the generated bounds
        public Integer[] getRawStackSizes() {
            return new Integer[]{10};
        }

        public Integer[] getRawLoOffsets() {
            return new Integer[]{};
        }

        public Integer[] getRawHiOffsets() {
            return new Integer[]{};
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws SecurityException {
            Integer[][] presetSizeIndexPairs = getPresetSizeIndexPairs();
            Integer[] rawLoOffsets = getRawLoOffsets();
            Integer[] rawHiOffsets = getRawHiOffsets();
            Integer[] rawStackSizes = getRawStackSizes();

            return   // Convert initial presets to Arguments.of(stackSize, testIndex)
                    Seq.of(presetSizeIndexPairs)
                            .map(Arguments::of)
                            // Generate unique combinations of values for lo and hi bounds
                            .append(generateSingleBoundValues(rawStackSizes, rawLoOffsets, t -> t.v2)       ) // 0  + offset
                            .append(generateSingleBoundValues(rawStackSizes, rawHiOffsets, t -> t.v1 + t.v2)) // hi + offset
                            .distinct();
        }
    }

    static abstract class AbstractOutOfBoundsProvider extends StackAndClearArgProvider {
        @Override
        public Integer[] getRawLoOffsets() {
            return new Integer[]{-1};
        }

        @Override
        public Integer[] getRawHiOffsets() {
            return new Integer[]{1};
        }
    }
    static class ValidAmountProvider extends StackAndClearArgProvider {
        //  for a stack size of 1
        @Override
        public Integer[][] getPresetSizeIndexPairs() {
            return new Integer[][]{{0, 0}, {1, 1}};
        }

        @Override
        public Integer[] getRawLoOffsets() {
            return new Integer[]{1};
        }

        @Override
        public Integer[] getRawHiOffsets() {
            return new Integer[]{-1};
        }
    }
//
//    static public class EmptyStackProvider extends AbstractOutOfBoundsProvider {
//        @Override
//        public Integer[][] getPresetSizeIndexPairs() {
//            return new Integer[][]{ {0}, {}
//        }
//    }

    static public class InvalidNumItemsProvider extends AbstractOutOfBoundsProvider {
        @Override
        public Integer[] getRawStackSizes() {
            Seq<Integer> s = Seq.of(0)
                    .append(nonEmptyListSizes());
            return  s.toArray(Integer[]::new);

        }
    }

}
