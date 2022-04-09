package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.organizer.NodeContext;
import com.mosect.parser4java.core.organizer.NodeOrganizer;
import com.mosect.parser4java.core.organizer.NodeRegion;
import com.mosect.parser4java.core.organizer.NodeRegionHandler;
import com.mosect.parser4java.java.NameConstants;
import com.mosect.parser4java.java.node.ImportNode;
import com.mosect.parser4java.java.token.KeywordToken;

public class ImportNodeRegionHandler extends BaseNodeRegionHandler {
    @Override
    public String getName() {
        return NameConstants.REGION_HANDLER_IMPORT;
    }

    @Override
    public Node createNode(NodeOrganizer organizer, NodeContext context, NodeRegion parent, int start, int offset) {
        return new ImportNode(NameConstants.NODE_IMPORT);
    }

    @Override
    protected boolean isRegionStartKeyword(KeywordToken token) {
        return "import".equals(token.getText());
    }

    @Override
    protected boolean isRegionEndSymbol(Token token) {
        return ";".equals(token.getText());
    }

    @Override
    public boolean isBrother(NodeRegionHandler other) {
        switch (other.getName()) {
            case NameConstants.REGION_HANDLER_IMPORT:
            case NameConstants.REGION_HANDLER_PACKAGE:
                return true;
        }
        return super.isBrother(other);
    }
}
