package com.github.pushfoo.jstacalc.vm.common;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.max;

/**
 * <p>A stack object with convenience behavior & methods</p>
 *
 * <p>In addition to following the Java doc's advice to use LinkedList instead
 * of the legacy Stack implementation, this class also adds the following convenience
 * features:</p>
 * <ul>
 *     <li>
 *         Grows towards high indices, i.e. push & pop occur
 *         on the right-hand side.
 *     </li>
 *     <li>push supports multiple elements at once from arrays & collections</li>
 *     <li>
 *         pop & peek support integer arguments specifying how many items to use.
 *         items to use.
 *     </li>
 *     <li>Partial VMStack-specific copyOf and copyOfRange re-implementations</li>
 * </ul>
 *
 * @param <E> The item type
 */
public class VMStack<E> extends LinkedList<E> {

    public static <E> VMStack<E> copyOfRange(
            Collection<? extends E> coll, int from, int to, E padding
    ) throws IndexOutOfBoundsException {

        IndexOutOfBoundsException problem = boundCheckNegative(to, coll);
        if ( Objects.nonNull(problem) ) {
            throw problem;
        }

        VMStack<E> newStack = new VMStack<E>();
        int originalSize = coll.size();
        int numFromColl  = originalSize - from;
        // Push the specified range onto the stack from coll
        coll
                .stream()
                .skip(from)
                .limit(numFromColl)
                .forEach(newStack::push);

        // Pad if necessary
        int numToPadWith = max(0, to - originalSize);
        newStack.padWith(numToPadWith, padding);

        return newStack;
    }

    public static <E> VMStack<E> copyOfRange(Collection<? extends E> coll, int from, int to) {
        return copyOfRange(coll, from, to, null);
    }

    public static <E> VMStack<E> copyOf(Collection<? extends E> coll, int newLength) {
        return copyOfRange(coll, 0, newLength);
    }

    public static <E> VMStack<E> copyOf(Collection<? extends E> coll) {
        return copyOf(coll, coll.size());
    }

    public static <E> VMStack<E> copyOf(E[] array, int newLength) {
        return copyOf(Arrays.stream(array).toList(), newLength);
    }

    public static <E> VMStack<E> copyOf(E[] array) {
        return copyOf(Arrays.stream(array).toList());
    }

    /**
     * Push the passed item onto the last index (right hand side) of the stack.
     *
     * @param item the element to push
     */
    public void push(E item) {
        addLast(item);
    }

    /**
     * Pop an item from the end index (right hand side) of the stack.
     *
     * @return A single element
     * @throws NoSuchElementException When the stack is empty
     */
    public E pop() throws NoSuchElementException {
        return removeLast();
    }

    /**
     * Return the top element of the stack without removing it.
     *
     * @return A single element
     */
    public E peek() {
        return peekLast();
    }

    /**
     * Push numCopies of the pad value to the end of the stack.
     *
     * @param numCopies how many copies to append.
     * @param pad       the pad value to push.
     */
    public void padWith(int numCopies, E pad) throws IllegalArgumentException {
        if ( numCopies < 0 ) throw new IllegalArgumentException("Can't have negative copies!");
        for (int left = numCopies; left > 0; left--) {
            push(pad);
        }
    }

    /**
     * If the stack is smaller than goalSize, push the pad value repeatedly until big enough.
     *
     * @param goalSize How big the stack should be
     * @param pad      The value to pad the end of the stack with if it's too small.
     * @return The difference between the goalSize and the initial size.
     */
    public int padTo(int goalSize, E pad) {
        int paddingRequired = goalSize - size();
        if (paddingRequired > 0) {
            padWith(paddingRequired, pad);
        }
        return paddingRequired;
    }

    private static <E> IndexOutOfBoundsException boundCheckNegative(int numItemsRequested, Collection <E> source) {
        if (numItemsRequested < 0) {
            return new IndexOutOfBoundsException(String.format(
                    "Can't have negative amounts (%d) of items", numItemsRequested));
        }
        return null;
    }
    private static <E> IndexOutOfBoundsException boundCheckAmountRequired(int numItemsRequested, Collection<E> source) {
        IndexOutOfBoundsException problem = null;
        problem = boundCheckNegative(numItemsRequested, source);
        if (numItemsRequested > source.size()) {
            return new IndexOutOfBoundsException(String.format(
                    "Only have %d items, but %d were requested", source.size(), numItemsRequested));
        }
        return problem;
    }

    /**
     * Return an exception if numItemsRequested is negative or larger than the stack size.
     *
     * @param numItemsRequested How many items we want to take from the top of the stack.
     * @return null, or the appropriate exception.
     */
    private IndexOutOfBoundsException boundCheckAmountRequired(int numItemsRequested) {
        return boundCheckAmountRequired(numItemsRequested, this);
    }

    /**
     * <p>Copy the top numItems elements to a new stack & return it.</p>
     *
     * <p>The elements will be in the same order as they were in the original
     * stack. An IndexOutOfBounds error will be raised on either of the following
     * conditions:</p>
     * <ul>
     *     <li>A negative amount of elements are requested</li>
     *     <li>More elements are requested than are in the stack</li>
     * </ul>
     *
     * @param numItems how many items to copy
     * @return The top numItems elements in the same order as this stack.
     * @throws IndexOutOfBoundsException When negative elements or
     */
    public VMStack<E> peek(int numItems) throws IndexOutOfBoundsException {
        IndexOutOfBoundsException problem = boundCheckAmountRequired(numItems);
        if (problem != null) {
            throw problem;
        }
        // Copy the top numItems entries to a new stack & return it
        return VMStack.copyOfRange(this, size() - numItems, size());
    }

    /**
     * <p>Discard the top numItems elements from the high end of the stack.</p>
     *
     * <p>An IndexOutOfBounds error will be raised on either of the following
     * conditions:</p>
     * <ul>
     *     <li>A negative amount of elements are requested</li>
     *     <li>More elements are requested than are in the stack</li>
     * </ul>
     *
     * @param numItems How many items to clear.
     * @throws IndexOutOfBoundsException When negative or too many items are requested.
     */
    public void clear(int numItems) throws IndexOutOfBoundsException {
        IndexOutOfBoundsException problem = boundCheckAmountRequired(numItems);
        if (problem != null) {
            throw problem;
        }
        for (int remaining = numItems; remaining > 0; remaining--) {
            pop();
        }
    }

    /**
     * <p>Remove multiple elements from the high end of the stack & return them.</p>
     *
     * <p>The elements will be in the same order as they were in the original
     * stack. An IndexOutOfBounds error will be raised on either of the following
     * conditions:</p>
     * <ul>
     *     <li>A negative amount of elements are requested</li>
     *     <li>More elements are requested than are in the stack</li>
     * </ul>
     *
     * @param numItems How many items to remove.
     * @return The top numItems elements in the stack
     * @throws IndexOutOfBoundsException When negative or too many items requested.
     * @throws NoSuchElementException When the stack is empty
     */
    public VMStack<E> pop(int numItems) throws IndexOutOfBoundsException, NoSuchElementException {
        VMStack<E> out = peek(numItems);
        clear(numItems);
        return out;
    }

    /**
     * Push all items from an iterable onto the high end of the stack.
     *
     * @param iterable The iterable to push items from.
     */
    public void push(Iterable<? extends E> iterable) {
        for (E item : iterable) {
            push(item);
        }
    }

    /**
     * Push all items from an iterator onto the high end of the stack.
     *
     * @param iterator The source iterator
     */
    public void push(Iterator<? extends E> iterator) {
        while (iterator.hasNext()) {
            push(iterator.next());
        }
    }

    /**
     * Push all elements from an array at the high end of the stack.
     *
     * @param array The source array to add elements from.
     */
    public void push(E[] array) {
        for (E item : array) {
            push(item);
        }
    }

}
