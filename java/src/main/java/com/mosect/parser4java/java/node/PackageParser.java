package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeSource;
import com.mosect.parser4java.java.NameConstants;
import com.mosect.parser4java.java.util.NodeUtils;

public class PackageParser extends BaseParser {

    protected RefPathParser refPathParser;

    public PackageParser(ParserFactory factory) {
        super(factory);
        refPathParser = getParseByName(NameConstants.PARSER_REF_PATH);
    }

    @Override
    public String getName() {
        return NameConstants.PARSER_PACKAGE;
    }

    @Override
    protected boolean onParse(NodeSource source, boolean newStart) {
        if (newStart) {
            Node currentNode = source.currentNode();
            // 查找 package
            if (NodeUtils.isKeywordNode(currentNode, "package")) {
                Node node = setNodeWithChild(currentNode);
                source.setStartAfterOffset();
                skipIgnore();
                if (refPathParser.parse(this, source)) {
                    node.addChild(refPathParser.getNode());
                }
                skipIgnore();
                currentNode = source.currentNode();
                if (source.hasMore() && NodeUtils.isSymbolNode(source.currentNode(), ";")) {
                    node.addChild(currentNode);
                    source.setStartAfterOffset();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected Node createNode() {
        return new PackageNode(NameConstants.NODE_PACKAGE);
    }
}
