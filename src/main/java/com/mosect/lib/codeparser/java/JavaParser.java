package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.Parser;

public class JavaParser extends Parser {

    public JavaParser() {
        addNodeParser(new CommentParser());
        addNodeParser(new StringParser());
        addNodeParser(new CharParser());
        addNodeParser(new WhitespaceParser());
        addNodeParser(new SymbolParser());
        addNodeParser(new WordParser());
    }
}
