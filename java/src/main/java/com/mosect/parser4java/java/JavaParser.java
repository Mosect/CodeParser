package com.mosect.parser4java.java;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.TextParser;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.util.ArrayNodeList;
import com.mosect.parser4java.core.util.ParserSet;
import com.mosect.parser4java.java.node.BlockNodeParser;
import com.mosect.parser4java.java.node.CurlyBracketNodeParser;
import com.mosect.parser4java.java.node.CurvesBracketNodeParser;
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
    protected CurlyBracketNodeParser curlyBracketNodeParser = new CurlyBracketNodeParser();
    protected CurvesBracketNodeParser curvesBracketNodeParser = new CurvesBracketNodeParser();
    protected BlockNodeParser blockNodeParser = new BlockNodeParser();

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

    public void parse(TextSource source, int start, Node out) {
        onClear();
        parserSet1.parse(source, start);

        // 解析花括号
        curlyBracketNodeParser.parse(new ArrayNodeList(tokenList), 0, tokenList.size(), out);
        // 解析圆括号
        List<Node> nodes = new ArrayList<>();
        findAllNodes(out, nodes);
        ArrayNodeList srcTemp = new ArrayNodeList(64);
        for (Node node : nodes) {
            curvesBracketNodeParser.organize(node, srcTemp);
        }
        // 解析块：语句、方法、类
        nodes.clear();
        findAllNodes(out, nodes);
        for (Node node : nodes) {
            blockNodeParser.organize(node, srcTemp);
        }
    }

    protected void findAllNodes(Node src, List<Node> out) {
        out.add(src);
        for (Node child : src) {
            if (child.isToken()) continue;
            out.add(child);
            findAllNodes(child, out);
        }
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
    }

    class InternalParserSet extends ParserSet {

        @Override
        protected void processParser(TextSource source, TextParser parser) {
            JavaParser.this.processParser(source, parser);
        }

        @Override
        protected boolean canParse(TextSource source, TextParser parser, int offset, int unknownStart) {
            if (Constants.PARSER_NUMBER.equals(parser.getName())) {
                return offset == unknownStart;
            }
            return true;
        }
    }
}
