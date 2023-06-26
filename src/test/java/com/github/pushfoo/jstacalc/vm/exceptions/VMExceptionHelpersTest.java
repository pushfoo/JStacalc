package com.github.pushfoo.jstacalc.vm.exceptions;

import org.jooq.lambda.Seq;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.jooq.lambda.Seq.crossJoin;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class VMExceptionHelpersTest {
    static final String[] wrappings = { "LONG", "\"", null};
    static final String[] inners = {"inner", "\"", null};

    static Stream<Arguments> wrapInnerBeforeAfterVersion() {
         return crossJoin(Seq.of(inners), Seq.of(wrappings), Seq.of(wrappings))
                 .map(t -> arguments(t.v1, t.v2, t.v3, t.v2 + t.v1 + t.v3));
    }

    @ParameterizedTest
    @MethodSource("wrapInnerBeforeAfterVersion")
    void givenWrapInnerBeforeAfterCalled_withStringOrNullArguments_resultIsProperlyFormatted(
            String inner, String before, String after, String expected
    ) {
        String result = VMException.wrap(inner, before, after);
        assertEquals(expected, result);
    }

    static Stream<Arguments> wrapInnerWrappingVersion() {
        return (Stream<Arguments>) crossJoin(Seq.of(inners), Seq.of(wrappings))
                .map(t -> arguments(t.v1, t.v2, t.v2 +  t.v1 + t.v2));
    }

    @ParameterizedTest
    @MethodSource("wrapInnerWrappingVersion")
    void givenWrapInnerWrappingVersionCalled_withStringOrNullArguments_resultIsProperlyFormatted(
            String inner, String wrapping, String expected
    ) {
        String result = VMException.wrap(inner, wrapping);
        assertEquals(expected, result);
    }

    static Stream<Arguments> singlequoteArgSource() {
        return (Stream<Arguments>) Seq.of(inners)
                .map(t -> arguments(t, "'" + t + "'"));
    }

    @ParameterizedTest
    @MethodSource("singlequoteArgSource")
    void givenSinglequoteCalled_withStringOrNullArgument_resultIsProperlyFormatted(
            String inner, String expected
    ) {
        String result = VMException.singlequote(inner);
        assertEquals(result, expected);
    }

    @ParameterizedTest
    @MethodSource("wrapInnerBeforeAfterVersion")
    void givenAppendToStringBuilderCalledWithStream_withStringOrNullArguments_resultIsProperlyFormatted(
            String inner, String before, String after, String expected
    ) {
        StringBuilder builder = new StringBuilder();
        VMException.appendToStringBuilder(builder, Stream.of(before, inner, after));
        assertEquals(expected, builder.toString());
    }

    @ParameterizedTest
    @MethodSource("wrapInnerBeforeAfterVersion")
    void givenAppendToStringBuilderCalledWithVarArgs_withStringOrNullArguments_resultIsProperlyFormatted(
            String inner, String before, String after, String expected
    ) {
        StringBuilder builder = new StringBuilder();
        VMException.appendToStringBuilder(builder, before, inner, after);
        assertEquals(expected, builder.toString());
    }
}