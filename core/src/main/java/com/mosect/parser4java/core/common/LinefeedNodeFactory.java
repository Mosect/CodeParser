package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.Token;

import java.util.List;

/**
 * 换行节点工厂
 */
public class LinefeedNodeFactory implements NodeFactory {

    public final static Token CR = new CommonToken("", "cr", "linefeed", "\r");
    public final static Token LF = new CommonToken("", "lf", "linefeed", "\n");
    public final static Token CRLF = new CommonToken("", "crlf", "linefeed", "\r\n");

    @Override
    public String getName() {
        return "linefeed";
    }

    @Override
    public Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        CharSequence text = source.getText();
        int pos = source.getPosition();
        char first = text.charAt(pos);
        if (first == '\r') {
            int nextPos = pos + 1;
            if (nextPos < text.length()) {
                char next = text.charAt(nextPos);
                if (next == '\n') {
                    // \r\n
                    source.setPosition(pos + 2);
                    return CRLF;
                }
            }
            source.setPosition(pos + 1);
            return CR;
        } else if (first == '\n') {
            source.setPosition(pos + 1);
            return LF;
        }
        return null;
    }

    public Token lf() {
        return LF;
    }

    public Token cr() {
        return CR;
    }

    public Token crlf() {
        return CRLF;
    }
}
