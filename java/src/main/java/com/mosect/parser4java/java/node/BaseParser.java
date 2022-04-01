package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeSource;
import com.mosect.parser4java.core.common.CommonNode;
import com.mosect.parser4java.core.common.CommonNodeParser;
import com.mosect.parser4java.java.NameConstants;

/**
 * 基础解析器
 */
public abstract class BaseParser extends CommonNodeParser {

    private final ParserFactory factory;
    protected IgnoreParser ignoreParser = null;

    public BaseParser(ParserFactory factory) {
        this.factory = factory;
    }

    public ParserFactory getFactory() {
        return factory;
    }

    @SuppressWarnings("unchecked")
    protected <T extends BaseParser> T getParseByName(String name) {
        return (T) getFactory().getByName(name);
    }

    protected Node setNodeWithChild(Node child) {
        Node node = createNode();
        node.addChild(child);
        setNode(node);
        return node;
    }

    /**
     * 跳过忽略的节点
     *
     * @param source    节点源
     * @param container 输出的容器节点
     */
    protected void skipIgnore(NodeSource source, Node container) {
        if (null == ignoreParser) ignoreParser = getParseByName(NameConstants.PARSER_IGNORE);

        while (source.hasMore()) {
            if (ignoreParser.parse(this, source)) {
                container.addChild(ignoreParser.getNode());
            } else {
                break;
            }
        }
    }

    /**
     * 跳过忽略的节点
     */
    protected void skipIgnore() {
        skipIgnore(getSource(), getNode());
    }

    protected Node createNode() {
        return new CommonNode(getName());
    }
}
