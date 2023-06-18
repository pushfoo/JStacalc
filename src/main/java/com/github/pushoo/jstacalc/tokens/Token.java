package com.github.pushoo.jstacalc.tokens;

/**
 * Track the position of a token in a file.
 * @param value    The string value of a token
 * @param position Its line & column value, and optionally a file path.
 */
public record Token(String value, FilePosition position) {
    /**
     * Short-form constructor which creates a file position with a null file path.
     * @param value The string value of a token.
     * @param line  The line number of a token.
     * @param col   The column number of a token.
     */
    public Token(String value, int line, int col)  {
        this(value, new FilePosition(line, col));
    }

    /**
     * Short-form constructor which creates a file position internally.
     * @param value    The token's string value
     * @param line     The line number of the token.
     * @param col      The column number of the token.
     * @param filepath The file path of the token.
     */
    public Token(String value, int line, int col, String filepath) {
        this(value, new FilePosition(line, col, filepath));
    }

    public String filepath() {
        return position.filepath();
    }

    public int line() {
        return position.line();
    }

    public int col() {
        return position.col();
    }

}
