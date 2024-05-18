package com.javasl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.javasl.compiler.Parser;
import com.javasl.compiler.Token;
import com.javasl.compiler.Tokenizer;
import com.javasl.compiler.ast.AST;

public class Script {
    public Script() {}
    public void compileFromFile(String path) throws IOException, IllegalArgumentException {
        // load file
        String content = null;
        try {
            content = Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new IOException("Loading error: Failed to load file " + path);
        }

        // tokenize
        Tokenizer tokenizer = new Tokenizer();
        ArrayList<Token> tokens = tokenizer.tokenize(content);
        if (tokens == null) {
            throw new IllegalArgumentException("Tokenizer error: Failed to tokenize file " + path);
        }

        // parse
        Parser parser = new Parser();
        AST ast = parser.parse(tokens);
        if (ast == null) {
            throw new IllegalArgumentException("Parser error: Failed to parse file " + path);
        }

        // TODO: compile

        m_ready = true;
    }

    public boolean isReady() {
        return m_ready;
    }
    private boolean m_ready = false;
}
