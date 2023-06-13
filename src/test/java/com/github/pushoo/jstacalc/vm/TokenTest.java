package com.github.pushoo.jstacalc.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {
    @Test
    void shortestFormConstructorSetsNullFilePath() {
        Token t = new Token("0", 0, 0);
        assertEquals(
            null, t.position().filepath(),
            "Expected short constructor form to set filepath to null"
        );
    }
}