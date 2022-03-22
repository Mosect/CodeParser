package com.mosect.parser4java.java;

import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.TextParseError;
import com.mosect.parser4java.core.TextParser;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.javalike.CharParser;
import com.mosect.parser4java.core.javalike.CommentParser;
import com.mosect.parser4java.core.javalike.StringParser;
import com.mosect.parser4java.core.javalike.SymbolParser;
import com.mosect.parser4java.core.javalike.WhitespaceParser;

import java.util.ArrayList;
import java.util.List;

/**
 * java解析器
 */
public class JavaParser {

    protected final List<TextParser> parsers1 = new ArrayList<>();
    protected final List<TextParser> parsers2 = new ArrayList<>();

    public JavaParser() {
        parsers1.add(new CommentParser());
        parsers1.add(new StringParser());
        parsers1.add(new CharParser());
        parsers1.add(new WhitespaceParser());
        parsers1.add(new SymbolParser());

        parsers2.add(new ValueParser());
        parsers2.add(new NameParser());
    }

    public void parse(TextSource source, int start) {
        CharSequence text = source.getText();
        FreeText freeText = new FreeText(text);
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
                handleParser2(source, freeText, unknownStart, validParser.getTextStart());
                unknownStart = validParser.getTextEnd();
                processParser(source, validParser);
            }
        }
        handleParser2(source, freeText, unknownStart, text.length());
    }

    protected void handleParser2(TextSource source, FreeText freeText, int unknownStart, int unknownEnd) {
        if (unknownEnd > unknownStart) {
            freeText.setLength(unknownEnd);
            for (TextParser parser : parsers2) {
                parser.parse(freeText, unknownStart);
                if (parser.isPass()) {
                    processParser(source, parser);
                    break;
                }
            }
        }
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
        }
        switch (parser.getName()) {
            case "java.name":
                NameParser nameParser = (NameParser) parser;
                if (!nameParser.isKeyword()) {
                    System.out.printf("%s >>> %s\n", parser.getName(), parser.getDisplayParseText());
                }
                break;
        }
    }

    protected static class FreeText implements CharSequence {

        private final CharSequence src;
        private int length;

        public FreeText(CharSequence src) {
            this.src = src;
            this.length = src.length();
        }

        public void setLength(int length) {
            this.length = length;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public char charAt(int i) {
            return src.charAt(i);
        }

        @Override
        public CharSequence subSequence(int s, int e) {
            return src.subSequence(s, e);
        }

        @Override
        public String toString() {
            return src.subSequence(0, length).toString();
        }
    }
}
