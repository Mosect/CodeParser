package com.mosect.parser4java.java;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.TextParser;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.util.ParserSet;
import com.mosect.parser4java.java.token.CharParser;
import com.mosect.parser4java.java.token.CommentParser;
import com.mosect.parser4java.java.token.NamedParser;
import com.mosect.parser4java.java.token.NumberParser;
import com.mosect.parser4java.java.token.StringParser;
import com.mosect.parser4java.java.token.SymbolParser;
import com.mosect.parser4java.java.token.TextBlockParser;
import com.mosect.parser4java.java.token.WhitespaceParser;

import java.util.ArrayList;
import java.util.List;

/**
 * java解析器
 */
public class JavaParser {

    protected ParserSet parserSet1;
    protected ParserSet parserSet2;

    protected final List<Token> tokenList = new ArrayList<>(512);
    private List<Node> nodes;

    public JavaParser() {
        // 第一层解析
        parserSet1 = new InternalParserSet();
        parserSet1.addParser(new CommentParser());
        parserSet1.addParser(new TextBlockParser());
        parserSet1.addParser(new StringParser());
        parserSet1.addParser(new CharParser());
        parserSet1.addParser(new WhitespaceParser());

        // 第二层解析
        parserSet2 = new InternalParserSet();
        parserSet2.addParser(new NumberParser());
        parserSet2.addParser(new SymbolParser());

        // 第三层解析
        ParserSet parserSet3 = new InternalParserSet();
        parserSet3.addParser(new NamedParser());

        parserSet2.setChildParserSet(parserSet3);

        parserSet1.setChildParserSet(parserSet2);
    }

    public void parse(TextSource source, int start) {
        onClear();
        parserSet1.parse(source, start);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    protected void processParser(TextSource source, TextParser parser) {
        if (parser.hasError()) {
            List<ParseError> errors = new ArrayList<>();
            for (int i = 0; i < parser.getErrorCount(); i++) {
                errors.add(parser.getError(i));
            }
            source.adjustErrors(errors);
            System.err.println(parser.getParseText());
            for (ParseError pe : errors) {
                System.err.println("charOffset: " + (pe.getPosition() - parser.getTextStart()));
                System.err.println(pe);
            }
            throw new IllegalStateException("Unsupported: " + parser.getParseText());
        }
        Token token = parser.makeToken();
        tokenList.add(token);
    }

    protected void onClear() {
        tokenList.clear();
        nodes = null;
    }

    class InternalParserSet extends ParserSet {

        @Override
        protected void processParser(TextSource source, TextParser parser) {
            JavaParser.this.processParser(source, parser);
        }

        @Override
        protected boolean canParse(TextSource source, TextParser parser, int offset, int unknownStart) {
            if (NameConstants.PARSER_NUMBER.equals(parser.getName())) {
                return offset == unknownStart;
            }
            return true;
        }
    }
}
