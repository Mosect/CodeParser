package com.mosect.parser4java.java;

import com.mosect.parser4java.core.TextParser;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.common.CommonToken;
import com.mosect.parser4java.core.util.WrapText;

import java.util.ArrayList;
import java.util.List;

/**
 * java解析器
 */
public class JavaParser {

    protected final List<TextParser> parsers1 = new ArrayList<>();
    protected final List<TextParser> parsers2 = new ArrayList<>();

    protected final List<Token> tokenList = new ArrayList<>(512);

    protected WhitespaceTokenFactory whitespaceTokenFactory = new WhitespaceTokenFactory();

    public JavaParser() {
        parsers1.add(new CommentParser());
        parsers1.add(new StringParser());
        parsers1.add(new CharParser());
        parsers1.add(new WhitespaceParser());
        parsers1.add(new SymbolParser());

        parsers2.add(new NameParser());
    }

    public void parse(TextSource source, int start) {
        onClear();

        CharSequence text = source.getText();
        WrapText wrapText = new WrapText(text);
        int unknownStart = start;
        int offset = start;
        while (offset < text.length()) {
            TextParser validParser = null;
            for (TextParser parser : parsers1) {
                parser.parse(text, offset);
                if (parser.isPass()) {
                    validParser = parser;
                    break;
                }
            }
            if (null == validParser) {
                ++offset;
            } else {
                offset = validParser.getTextEnd();
                handleParser2(source, wrapText, unknownStart, validParser.getTextStart());
                unknownStart = validParser.getTextEnd();
                processParser(source, validParser);
            }
        }
        handleParser2(source, wrapText, unknownStart, text.length());
    }

    protected void handleParser2(TextSource source, WrapText wrapText, int unknownStart, int unknownEnd) {
        if (unknownEnd > unknownStart) {
            wrapText.setLength(unknownEnd);
            for (TextParser parser : parsers2) {
                parser.parse(wrapText, unknownStart);
                if (parser.isPass()) {
                    processParser(source, parser);
                    break;
                }
            }
        }
    }

    protected boolean processParser(TextSource source, TextParser parser) {
        /*if (parser.hasError()) {
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
        }*/
        switch (parser.getName()) {
            case "java.name":
                NameParser nameParser = (NameParser) parser;
                break;
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

    protected void onClear() {
        tokenList.clear();
    }
}
