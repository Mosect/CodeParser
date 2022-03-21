package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 空白字符节点解析器
 */
public class WhitespaceNodeFactory implements NodeFactory {

    public final static Token SPACE = new CommonToken("space", "space", "whitespace", " ");
    public final static Token TAB = new CommonToken("tab", "tab", "whitespace", "\t");

    private final Map<String, Token> tokenMap = new HashMap<>();

    public WhitespaceNodeFactory() {
        register(SPACE);
        register(TAB);
    }

    public Token register(String name, String text) {
        CommonToken token = new CommonToken(name, name, "whitespace", text);
        tokenMap.put(token.getId(), token);
        return token;
    }

    public void register(Token token) {
        tokenMap.put(token.getId(), token);
    }

    public Token unregister(String id) {
        return tokenMap.remove(id);
    }

    @Override
    public String getName() {
        return "whitespace";
    }

    @Override
    public Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        for (Token token : tokenMap.values()) {
            if (source.match(token.getText())) {
                source.offset(token.getText().length());
                return token;
            }
        }
        return null;
    }
}
