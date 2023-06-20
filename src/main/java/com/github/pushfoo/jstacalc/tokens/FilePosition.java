package com.github.pushfoo.jstacalc.tokens;

import java.util.Objects;

public record FilePosition(int line, int col, String filepath) {

    public FilePosition(int line, int col) {
        this(line, col, null);
    }

}
