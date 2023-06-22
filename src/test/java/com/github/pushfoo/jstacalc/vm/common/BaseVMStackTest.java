package com.github.pushfoo.jstacalc.vm.common;

import org.jooq.lambda.Seq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p>A base class for VMStack tests.</p>
 *
 * <p>Features include:</p>
 * <ul>
 *      <li>Initializing a common tested stack</li>
 *      <li>Static methods & variables to make up for JUnit 5's rough edges</li>
 * </ul>
 */
public abstract class BaseVMStackTest {
    // Used for padding tests
    public static final int sentinelPadValue = 99;

    protected VMStack<Integer> testOperationStack;

    public static Stream<Arguments> asArgs(Stream<?> s) {
        return s.map(Arguments::of);
    }

    public static Stream<Arguments> asArgs(Seq<?> s) {
        return s.map(Arguments::of).stream();
    }
    public static Seq<Integer> rangeSeq(int to) {
        return Seq.range(0, to);
    }

    public static Stream<Integer> rangeStream(int to) {
        // IntStream + boxed() prevent typing issues compared to jOOL's Seq
        return IntStream.range(0, to).boxed();
    }

    public static List<Integer> rangeList(int to) {
        return rangeStream(to).toList();
    }

    public static Integer[] rangeArray(int to) {
        return rangeStream(to).toArray(Integer[]::new);
    }

    public static <E> Stream<E> repeatNTimesStream(int nTimes, E arg) {
        return Seq.of(arg).cycle(nTimes);
    }

    public static <E> List<E> repeatNTimesList(int nTimes, E arg) {
        return repeatNTimesStream(nTimes, arg).toList();
    }

    public static <E> void fillStackFromFunction(VMStack<E> stack, int size, Function<Integer, E> f) {
        if (size < 0) throw new IndexOutOfBoundsException("Can't have negative number of items!");
        if (size == 0) return;
        for (int i = 0; i < size; i++) stack.push(f.apply(i));
    }

    public static void fillStackWithIndices(VMStack<Integer> stack, int size) throws IndexOutOfBoundsException {
        fillStackFromFunction(stack, size, index -> index);
    }

    static Seq<Integer> nonEmptyListSizes() {
        return Seq.of(1,2,5);
    }

    static Stream<Arguments> nonEmptyListSizesArguments() {
        return asArgs(nonEmptyListSizes());
    }

    static Seq<Integer> nonZeroPaddingSizes() {
        return Seq.of(1, 5, 10);
    }

    static Stream<Arguments> paddingSizesArguments() {
        return asArgs(nonZeroPaddingSizes());
    }

    /**
     * Inherit from this to set up an empty value for testOperationStack
     */
    static class EmptyVMStackTest extends BaseVMStackTest {

        @BeforeEach
        void setup() {
            testOperationStack = new VMStack<>();
        }
    }

}
