package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.organizer.NodeContext;
import com.mosect.parser4java.core.organizer.NodeOrganizer;
import com.mosect.parser4java.core.organizer.NodeRegion;
import com.mosect.parser4java.core.organizer.NodeRegionHandler;
import com.mosect.parser4java.java.NameConstants;
import com.mosect.parser4java.java.node.PackageNode;
import com.mosect.parser4java.java.token.KeywordToken;

public class PackageNodeRegionHandler extends BaseNodeRegionHandler {

    @Override
    public String getName() {
        return NameConstants.REGION_HANDLER_PACKAGE;
    }

    @Override
    public Node createNode(NodeContext context, NodeRegion parent, int start, int offset) {
        return new PackageNode(NameConstants.NODE_PACKAGE);
    }

    @Override
    protected boolean isRegionStartKeyword(KeywordToken token) {
        return "package".equals(token.getText());
    }

    @Override
    protected boolean isRegionEndSymbol(Token token) {
        return ";".equals(token.getText());
    }
}
