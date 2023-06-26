package com.github.pushfoo.jstacalc.vm.exceptions;

import java.util.List;
import java.util.stream.Stream;

public class VMException extends Exception {

    public VMException() {
        super();
    }
    public VMException(String message) {
        super(message);
    }

    public VMException(String message, Throwable cause) {
        super(message, cause);
    }
    public VMException(Throwable cause) {
        super(cause);
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
}
