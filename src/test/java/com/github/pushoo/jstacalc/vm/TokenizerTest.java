package com.github.pushoo.jstacalc.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void resetResetsState() {
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.processLine("1 2 3");
        tokenizer.reset();
        Token[] tokens = tokenizer.getTokens();
        assertEquals(0, tokens.length,
                "tokens.length should be zero");
        assertEquals(0, tokenizer.getLineNumber(),
                "line number should be reset to zero");
    }

    @Test
    void processLineRaisesNullPointerExceptionOnNullLine(){
        Tokenizer t = new Tokenizer();
        assertThrows(NullPointerException.class, () -> t.processLine(null, "filepath"));
    }

    @Test
    void processLineExtractsTokensCorrectly() {
        Tokenizer t = new Tokenizer();
        t.processLine("1 2 3");
        Token[] tokens = t.getTokens();

        // Make sure the parse result is correct
        assertEquals(tokens.length, 3,
                "Expected 3 tokens");

        // Make sure token values are as expected
        for (int i = 0; i < tokens.length; i++) {
            Token        token    = tokens[i];
            String       value    = token.value();
            FilePosition p        = token.position();

            String expectedValue = String.format("%d", i + 1);
            assertEquals(
                    expectedValue, value,
                    String.format("Expected token value to be '%s'", expectedValue));

            assertEquals(
                    1, p.line(),
                    "Expected line number to be 1");

            int expectedColumn = 2 * i;
            assertEquals(expectedColumn, p.col(),
                    String.format("Expected '%s' to have column value %d", value, expectedColumn));
        }
    }
}