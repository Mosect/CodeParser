package com.mosect.parser4java.java.util;

import com.mosect.parser4java.core.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * 符号边界节点组织器
 */
public class SymbolBoundNodeOrganizer {

    private final List<String> symbols = new ArrayList<>();

    public SymbolBoundNodeOrganizer() {
        addSymbol("{", "}");
        addSymbol("<", ">");
        addSymbol("[", "]");
        addSymbol("(", ")");
    }

    public void organize(List<? extends Node> nodes, int offset, List<Node> outList) {
        int curOffset = offset;
        while (curOffset < nodes.size()) {
            Node curNode = nodes.get(curOffset);
            int symbolIndex = getEndBoundSymbol(curNode);
            if (symbolIndex >= 0) {
                String start = symbols.get(symbolIndex);
                String end = symbols.get(symbolIndex + 1);
                Node node = createNode(start, end);
                node.addChild(curNode);
                outList.add(node);
            } else {
                outList.add(curNode);
                ++curOffset;
            }
        }
    }

    public void clearSymbols() {
        symbols.clear();
    }

    public void addSymbol(String start, String end) {
        symbols.add(start);
        symbols.add(end);
    }

    protected int getEndBoundSymbol(Node node) {
        for (int i = 0; i < symbols.size(); i += 2) {
            String start = symbols.get(i);
            String end = symbols.get(i);
            if (NodeUtils.isSymbolNode(node, start)) {
                return i;
            }
        }
        return -1;
    }

    protected Node createNode(String start, String end) {

    }
}
