package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeSource;
import com.mosect.parser4java.java.NameConstants;

/**
 * 忽略节点解析器，包括注释、空白字符
 */
public class IgnoreParser extends BaseParser {

    public IgnoreParser(ParserFactory factory) {
        super(factory);
    }

    @Override
    public String getName() {
        return NameConstants.PARSER_IGNORE;
    }

    @Override
    protected boolean onParse(NodeSource source, boolean newStart) {
        if (newStart) {
            Node node = source.currentNode();
            if (node.isToken()) {
                switch (node.getType()) {
                    case "java.comment":
                    case "java.whitespace":
                        setNode(node);
                        source.setStartAfterOffset();
                        return true;
                }
            }
        }
        return false;
    }
}
