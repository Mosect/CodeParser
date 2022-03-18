package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 符号节点工厂
 */
public class SymbolNodeFactory implements NodeFactory {

    private final Map<String, SymbolToken> symbolTokenMap = new HashMap<>();

    public Set<String> symbolKeys() {
        return symbolTokenMap.keySet();
    }

    public SymbolToken getSymbol(String id) {
        return symbolTokenMap.get(id);
    }

    public Collection<SymbolToken> allSymbols() {
        return symbolTokenMap.values();
    }

    public void registerSymbol(SymbolToken token) {
        symbolTokenMap.put(token.getId(), token);
    }

    public void registerSymbol(String text) {
        registerSymbol(new SymbolToken(text));
    }

    public void unregisterSymbol(SymbolToken token) {
        symbolTokenMap.remove(token.getId());
    }

    public void unregisterSymbol(String id) {
        symbolTokenMap.remove(id);
    }

    public void unregisterSymbolWithText(String text) {
        symbolTokenMap.remove(new SymbolToken(text).getId());
    }

    @Override
    public String getName() {
        return "symbol";
    }

    @Override
    public Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        for (SymbolToken token : allSymbols()) {
            if (source.match(token.getText())) {
                return token;
            }
        }
        return null;
    }
}
