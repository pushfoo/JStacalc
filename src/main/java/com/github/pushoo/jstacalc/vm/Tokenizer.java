package com.github.pushoo.jstacalc.vm;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.*;


// TODO: look into converting this to a Stream<Token> implementation?
/**
 * <p>A tokenizer class which optionally takes a regex string for a valid token in a line.</p>
 *
 * <p>It allows concatenating inputs to generate one sequence of tokens from multiple
 * sources, as well as resetting its state to clear the current line number.</p>
 */
public class Tokenizer {

    private final Pattern          tokenPattern;
    private final ArrayList<Token> currentTokens;
    private       int              lineNumber;

    public int getLineNumber() {
        return lineNumber;
    }

    public Token[] getTokens() {
         return currentTokens.toArray(new Token[0]);
    }
    /**
     * <p>Build a tokenizer which uses the passed regex to delimit tokens.</p>
     *
     * <p>Note that this does not assign a type to a token, store its value
     * and its </p>
     *
     * @param patternString The regex string to recognize tokens through.
     */
    public Tokenizer(String patternString) {
        // create the token boundary regex object, defaulting to any non-whitespace
        tokenPattern  = Pattern.compile(patternString);
        lineNumber    = 0;
        currentTokens = new ArrayList<Token>();
    }

    /**
     * Default non-whitespace constructor.
     */
    public Tokenizer() {
        this("[^ \n\t]+");
    }

    /**
     * Reset the line number to zero and clear tokens from the token buffer.
     */
    public void reset(){
        lineNumber = 0;
        currentTokens.clear();
    }

    /**
     * Increment the current line number, then extract tokens to the internal store.
     * @param line     A single line as a string, ie only one newline is allowed.
     * @param filepath A file path which may be null.
     */
    public void processLine(String line, String filepath) {
        lineNumber += 1;
        Objects.requireNonNull(line);

        // Set up local state tracking
        Matcher     matcher      = tokenPattern.matcher(line);
        MatchResult currentMatch;
        String      currentValue;
        int         col;

        // Iterate through all matches in this line
        while (matcher.find()) {
            currentMatch = matcher.toMatchResult();
            col          = currentMatch.start();
            currentValue = line.substring(col, currentMatch.end());
            Token token  = new Token(currentValue, lineNumber, col, filepath);
            currentTokens.add(token);
        }
    }
    public void processLine(String line) {
        processLine(line, null);
    }

    /**
     * Iterate over all lines in an input source, converting them to tokens.
     *
     * @param rawInputStream a raw InputStream to process.
     * @param filepath       an optional indication of a file source, can be null.
     * @throws IOException   raised when something goes wrong.
     */
    public void process(InputStream rawInputStream, String filepath) throws IOException {
        BufferedReader bufferedReader = IOUtils.toBufferedReader(
                new InputStreamReader(rawInputStream, StandardCharsets.UTF_8));
        String line;
        while ( (line =  bufferedReader.readLine()) != null) {
            processLine(line, filepath);
        }
    }
    public void process(String input, String filepath) throws IOException {
       process(new ByteArrayInputStream(input.getBytes()), filepath);
    }

    public void process(String input) throws IOException {
        process(input, null);
    }

}
