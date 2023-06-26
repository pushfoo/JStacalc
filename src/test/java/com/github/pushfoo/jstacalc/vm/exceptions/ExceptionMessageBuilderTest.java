package com.github.pushfoo.jstacalc.vm.exceptions;

import com.github.pushfoo.jstacalc.tokens.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.jooq.lambda.Seq.crossJoin;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionMessageBuilderTest {

    ExceptionMessageBuilder builder;
    @BeforeEach
    void setup() {
        builder = new ExceptionMessageBuilder();
    }
    static Stream<Arguments> separatorSource() {
        return Stream.of("\n", " | ", ", ")
                .map(Arguments::arguments);
    }

    static class Dummy {
        public String toString() {
            return "object!";
        }
    }

    static Stream<Arguments> tokenSource() {
        return crossJoin(
                Stream.of("1", "word"),
                Stream.of(1, 5, 100),
                Stream.of(1, 5, 100),
                Stream.of("path/to/file", null)
        )
                .map(t -> Arguments.of(new Token(t.v1, t.v2, t.v3, t.v4)));
    }

    @ParameterizedTest
    @MethodSource("tokenSource")
    void givenTokenToPrependLocationOf_withDummyTopLevel_prependsLocationCorrectly(Token token) {
        builder.prepend(new Dummy());
        builder.prependLocation(token);
        String expected = "line %d, col %d%s: object!".formatted(
                token.line(), token.col(),
                nonNull(token.filepath()) ? " in %s".formatted(token.filepath()) : ""
        );
        assertEquals(expected, builder.toString());
    }

    @Test
    void getGivenExceptionMessageBuilderWithDefaultSeparator_whenPrependCalledMultipleTimns_grammaticalOrderPreserved(
    ) {
        builder.prepend(new Dummy());
        builder.prepend("Second ", null);
        builder.prepend("First ", 1);
        assertEquals("First 1: Second null: object!", builder.toString());
    }

    @ParameterizedTest
    @MethodSource("separatorSource")
    void getGivenExceptionMessageBuilderWithCustomSeparator_whenPrependCalledMultipleTimes_grammaticalOrderPreserved(
            String separator
    ) {
        ExceptionMessageBuilder builder = new ExceptionMessageBuilder(separator);
        builder.prepend(new Dummy());
        builder.prepend("Second ", null);
        builder.prepend("First ", 1);
        assertEquals("First 1%sSecond null%sobject!".formatted(separator, separator), builder.toString());
    }

}