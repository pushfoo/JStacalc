package com.github.pushoo.jstacalc.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilePositionTest {
    @Test
    void shortConstructorSetsFilePathToNull() {
        FilePosition f = new FilePosition(1,1);
        assertEquals(null, f.filepath(),
                "The file path should default to null");
    }
}