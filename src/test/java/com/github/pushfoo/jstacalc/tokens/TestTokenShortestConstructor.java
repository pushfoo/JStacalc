package com.github.pushfoo.jstacalc.tokens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestTokenShortestConstructor {
    Token token;
    int line;
    int col;

    @BeforeEach
    public void setup() {
        line = 10;
        col = 5;
        token = new Token("testValue", line, col);
    }

    @Test
    void filePositionAccessibleAndCorrect() {
        assertEquals(
                new FilePosition(line, col),
                token.position()
        );
    }

    @Test
    void setsNullFilePath() {
        assertNull(
                token.filepath(),
                "Expected short constructor form to set filepath to null"
        );
    }

    @Test
    void valueAccessibleAndCorrect() {
        assertEquals("testValue", token.value());
    }

    @Test
    void lineGetterReturnsCorrectValue() {
        assertEquals(
                line, token.line(),
                "line getter should be equal to the initial value"
        );
    }

    @Test
    void colGetterReturnsCorrectValue() {
        assertEquals(
                col, token.col(),
                "col getter should be equal to the initial value"
        );
    }


}