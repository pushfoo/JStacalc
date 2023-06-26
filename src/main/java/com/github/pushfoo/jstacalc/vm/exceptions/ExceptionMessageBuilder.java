package com.github.pushfoo.jstacalc.vm.exceptions;

import com.github.pushfoo.jstacalc.tokens.Token;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;


/**
 * <p>This is an answer to templating variables before we have a class initialized.<p>
 *
 * <p>In addition to static utility methods, this provides left-growing-ish behavior
 * for prepending. The items in each prepend call are added to the left-growing stack
 * to preserve their apparent grammatical order. This is useful for nested exception
 * hierarchies which may have multiple templated message components which would otherwise
 * spend a lot of time copying in String.format or similar calls.</p>
 *
 * <p>For example, see the following code:
 * <pre>
 *  ExceptionMessageBuilder builder = new ExceptionMessageBuilder(".\n");
 *  builder
 *       .prepend("The first message segment has an integer == ", 1);
 *       .prepend("The second message segment has an integer == ", 2);
 *       .prepend("The third message segment has an object == ", null);
 *  System.out.println(builder.toString);
 * </pre>
 * It will produces the following output:
 * <pre>
 *  The first message segment has an integer == 1.
 *  The second message segment has an integer == 2.
 *  The third message segment has an object == null.
 * </pre>
 * </p>
 *
 * </p>But why, you ask?</p>
 * <ol>
 *     <li>Super must be the first line in a constructor if present</li>
 *     <li>Instance variables are neither initialized nor accessible before super is called.</li>
 *     <li>A limited workaround for StringBuilder being final with a similar API.</li>
 * </ol>
 *
 * <p>This class passes a message templating object down through the class hierarchy
 * to allow a final exception message to be templated in the base class. Then, once
 * instance variables are set as usual as control propagates back up through the exception
 * class hierarchy.
 */
public class ExceptionMessageBuilder {

    // This is allocated first to allow us to temp we can use it later to template
    // the final message.
    private final StringBuilder outerBuilder;

    // Provides arbitrary length, and the linear access time doesn't
    // matter because we don't need random access for this class.
    private final LinkedList<String> appendOnLeftStructure;
    public  final String messageSeparator;

    public ExceptionMessageBuilder(String messageSeparator) {
        this.messageSeparator = messageSeparator;
        outerBuilder = new StringBuilder();
        appendOnLeftStructure = new LinkedList<>();
    }

    public ExceptionMessageBuilder() {
        this(": ");
    }

    public static String wrap(String inner, String before, String after) {
        return "%s%s%s".formatted(before, inner, after);
    }

    public static String wrap(String inner, String wrapping) {
        return wrap(inner, wrapping, wrapping);
    }

    public static String singlequote(String inner) {
        return wrap(inner, "'");
    }

    public static void appendToStringBuilder(StringBuilder builder, Stream<Object> stream) {
        stream.forEachOrdered(builder::append);
    }

    public static void appendToStringBuilder(StringBuilder builder, Object... objects) {
        appendToStringBuilder(builder, Stream.of(objects));
    }

    public static void appendToStringBuilder(StringBuilder builder, List<Object> objects) {
        appendToStringBuilder(builder, objects.stream());
    }

    public static StringBuilder appendToNewStringBuilder(Stream<Object> stream) {
        StringBuilder stringBuilder = new StringBuilder();
        stream.forEachOrdered(stringBuilder::append);
        return stringBuilder;
    }

    public static StringBuilder appendToNewStringBuilder(List<Object> list) {
        return appendToNewStringBuilder(list.stream());
    }

    public static StringBuilder appendToNewStringBuilder(Object... objects) {
        return appendToNewStringBuilder(Stream.of(objects));
    }

    public ExceptionMessageBuilder prepend(List<Object> list) {
        appendOnLeftStructure.offerFirst(appendToNewStringBuilder(list).toString());
        return this;
    }

    public ExceptionMessageBuilder prepend(Object... args) {
        return prepend(Arrays.asList(args));
    }

    public ExceptionMessageBuilder prepend(Stream<Object> stream) {
        return prepend(stream.toList());
    }

    public String toString() {
        Iterator<String> it = appendOnLeftStructure.iterator();
        String current;
        while( it.hasNext() ) {
            current = it.next();
            outerBuilder.append(current);
            if ( it.hasNext() ) {
                outerBuilder.append(messageSeparator);
            }
        }

        return outerBuilder.toString();
    }

    public ExceptionMessageBuilder prependLocation(Token token) {
        if (nonNull(token.filepath())) {
            prepend( "line ", token.line(), ", col ", token.col(), " in ", token.filepath());
        } else {
            prepend("line ", token.line(), ", col ", token.col());
        }
        return this;
    }
}
