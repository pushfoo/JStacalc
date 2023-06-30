package com.github.pushfoo.jstacalc.vm;

import org.jooq.lambda.Seq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VMCoreTest {
    VMCore core;
    @BeforeEach
    void setup() {
       core = new VMCore();
    }

    static Stream<Arguments> initialStackStateSource() {
        return Seq.range(0, 11, 2)
                .map(i -> Arguments.of((Object) Seq.range(0, i).toList()));
    }

    @ParameterizedTest
    @MethodSource("initialStackStateSource")
    void whenDataStackStateCalled_givenStackHasInitialState_resultIsEquivalent(List<Integer> initialState) {
        core.push(initialState.toArray(Integer[]::new));
        List<Integer> readOnly = core.dataStackState();
        assertIterableEquals(initialState, readOnly);
    }

}