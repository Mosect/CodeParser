package com.mosect.lib.codeparser.gradle;

import com.mosect.lib.codeparser.Parser;
import com.mosect.lib.codeparser.java.CommentParser;
import com.mosect.lib.codeparser.java.WhitespaceParser;

public class GradleParser extends Parser {

    public GradleParser() {
        addNodeParser(new CommentParser());
        addNodeParser(new PlainStringParser());
        addNodeParser(new DynamicStringParser());
        addNodeParser(new SymbolParser());
        addNodeParser(new WhitespaceParser());
        addNodeParser(new WordParser());
    }
}
