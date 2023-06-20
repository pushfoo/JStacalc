package com.github.pushfoo.jstacalc.tokens;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class FilePositionTest {
    @Test
    void shortConstructorSetsFilePathToNull() {
        FilePosition f = new FilePosition(1, 1);
        assertNull(f.filepath(), "The file path should default to null");
    }
}