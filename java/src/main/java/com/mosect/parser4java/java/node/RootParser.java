package com.mosect.parser4java.java.node;

import com.mosect.parser4java.core.NodeParser;
import com.mosect.parser4java.core.NodeSource;
import com.mosect.parser4java.java.NameConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 根解析器
 */
public class RootParser extends BaseParser {

    protected List<NodeParser> childParsers = new ArrayList<>();

    public RootParser(ParserFactory factory) {
        super(factory);

        registerParsers(NameConstants.PARSER_IGNORE,
                NameConstants.PARSER_PACKAGE, NameConstants.PARSER_IMPORT,
                NameConstants.PARSER_CLASS, NameConstants.PARSER_ENUM, NameConstants.PARSER_INTERFACE, NameConstants.PARSER_ANNOTATION
        );
    }

    @Override
    public String getName() {
        return NameConstants.PARSER_ROOT;
    }

    @Override
    protected boolean onParse(NodeSource source, boolean newStart) {
        if (!newStart) {
            return false;
        }

        DocumentNode documentNode = new DocumentNode();
        setNode(documentNode);
        while (source.hasMore()) {
            boolean ok = false;
            for (NodeParser parser : childParsers) {
                if (parser.parse(this, source)) {
                    ok = true;
                    // 解析成功
                    documentNode.addChild(parser.getNode());
                    break;
                }
            }
            if (!ok) {
                source.offsetOne();
            }
        }

        if (source.isValidStart()) {
            // 含有无法识别的节点
            source.resetOffset();
            while (source.hasMore()) {
                documentNode.addChild(source.currentNode());
                source.offsetOne();
            }

            // 含有无法处理的节点
            putError("JAVA_UNEXPECTED_NODE", "Document has unexpected node", source.computeStartPosition());
        }

        return true;
    }

    protected void registerParsers(String... names) {
        for (String name : names) {
            NodeParser parser = getParseByName(name);
            childParsers.add(parser);
        }
    }
}
