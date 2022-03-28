package com.mosect.parser4java.java;

import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.TextParseError;
import com.mosect.parser4java.core.TextParser;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.common.CommonToken;
import com.mosect.parser4java.core.util.ParserSet;

import java.util.ArrayList;
import java.util.List;

/**
 * java解析器
 */
public class JavaParser {

    protected ParserSet parserSet1;
    protected ParserSet parserSet2;

    protected final List<Token> tokenList = new ArrayList<>(512);

    protected WhitespaceTokenFactory whitespaceTokenFactory = new WhitespaceTokenFactory();

    public JavaParser() {
        // 第一遍解析
        parserSet1 = new ParserSet() {
            @Override
            protected void processParser(TextSource source, TextParser parser) {
                JavaParser.this.processParser(source, parser);
            }
        };
        parserSet1.addParser(new CommentParser());
        parserSet1.addParser(new StringParser());
        parserSet1.addParser(new CharParser());
        parserSet1.addParser(new WhitespaceParser());

        // 第二遍解析
        parserSet2 = new ParserSet() {
            @Override
            protected void processParser(TextSource source, TextParser parser) {
                JavaParser.this.processParser(source, parser);
            }
        };
        parserSet2.addParser(new NumberParser());
        parserSet2.addParser(new SymbolParser());
        parserSet2.addParser(new NamedParser());

        parserSet1.setChildParserSet(parserSet2);
    }

    public void parse(TextSource source, int start) {
        onClear();
        parserSet1.parse(source, start);
    }

    protected void processParser(TextSource source, TextParser parser) {
        if (parser.hasError()) {
            List<ParseError> errors = new ArrayList<>();
            for (int i = 0; i < parser.getErrorCount(); i++) {
                TextParseError error = parser.getError(i);
                ParseError parseError = new ParseError(error.getErrorId(), error.getErrorMsg(), error.getErrorPosition());
                errors.add(parseError);
            }
            source.adjustErrors(errors);
            System.err.println(parser.getParseText());
            for (ParseError pe : errors) {
                System.err.println("charOffset: " + (pe.getPosition() - parser.getTextStart()));
                System.err.println(pe);
            }
            throw new IllegalStateException("Unsupported: " + parser.getParseText());
        }
        boolean handled = onHandleToken(source, parser);
        if (!handled) {
            throw new IllegalStateException("Unsupported parser: " + parser.getName());
        }
    }

    protected boolean onHandleToken(TextSource source, TextParser parser) {
        switch (parser.getName()) {
            case "java.name":
                NamedParser namedParser = (NamedParser) parser;
                KeywordToken keywordToken = namedParser.getKeywordToken();
                if (null != keywordToken) {
                    tokenList.add(keywordToken);
                } else {
                    Token namedToken = createNamedToken(namedParser);
                    tokenList.add(namedToken);
                }
                return true;
            case "java.char":
                CharParser charParser = (CharParser) parser;
                CharToken charToken = createCharToken(charParser);
                tokenList.add(charToken);
                return true;
            case "java.string":
                StringParser stringParser = (StringParser) parser;
                StringToken stringToken = createStringToken(stringParser);
                tokenList.add(stringToken);
                return true;
            case "java.comment":
                CommentParser commentParser = (CommentParser) parser;
                CommentToken commentToken = createCommentToken(commentParser);
                tokenList.add(commentToken);
                return true;
            case "java.symbol":
                SymbolParser symbolParser = (SymbolParser) parser;
                Token symbolToken = createSymbolToken(symbolParser);
                tokenList.add(symbolToken);
                return true;
            case "java.whitespace":
                WhitespaceParser whitespaceParser = (WhitespaceParser) parser;
                WhitespaceToken whitespaceToken = createWhitespaceToken(whitespaceParser);
                tokenList.add(whitespaceToken);
                return true;
            case "java.number":
                NumberParser numberParser = (NumberParser) parser;
                NumberToken numberToken = createNumberToken(numberParser);
                tokenList.add(numberToken);
                return true;
            default:
                return false;
        }
    }

    protected CharToken createCharToken(CharParser charParser) {
        String text = charParser.getParseText().toString();
        return new CharToken(charParser.getName(), text, charParser.getValue());
    }

    protected StringToken createStringToken(StringParser stringParser) {
        String text = stringParser.getParseText().toString();
        return new StringToken(stringParser.getName(), text, stringParser.getString());
    }

    protected CommentToken createCommentToken(CommentParser commentParser) {
        String text = commentParser.getParseText().toString();
        return new CommentToken(commentParser.getName(), text, commentParser.getCommentName());
    }

    protected Token createSymbolToken(SymbolParser symbolParser) {
        String text = symbolParser.getParseText().toString();
        return new CommonToken(symbolParser.getName(), text);
    }

    protected WhitespaceToken createWhitespaceToken(WhitespaceParser whitespaceParser) {
        String text = whitespaceParser.getParseText().toString();
        return whitespaceTokenFactory.createTokenByText(text);
    }

    protected NumberToken createNumberToken(NumberParser numberParser) {
        String text = numberParser.getParseText().toString();
        return new NumberToken(
                numberParser.getName(),
                text,
                numberParser.getValue(),
                numberParser.getRadix(),
                numberParser.isInteger()
        );
    }

    protected Token createNamedToken(NamedParser namedParser) {
        return new CommonToken("java.named", namedParser.getNameText());
    }

    protected void onClear() {
        tokenList.clear();
    }
}
