package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;
import com.mosect.parser4java.java.node.BraceNode;
import com.mosect.parser4java.java.node.CurvesNode;
import com.mosect.parser4java.java.node.SquareNode;

/**
 * 括号节点组织器，不包括尖括号
 */
public class BracketNodeOrganizer extends JavaNodeOrganizer {

    @Override
    protected JavaNodeRegion onMatchRegion(NodeList nodeList, Node node, int start, int offset) {
        if (node.isToken() && Constants.TOKEN_SYMBOL.equals(node.getType())) {
            Token token = (Token) node;
            switch (token.getText()) {
                case "{":
                    return new JavaNodeRegion(
                            new BraceNode(Constants.NODE_BRACE),
                            nodeList,
                            offset,
                            offset + 1
                    );
                case "(":
                    return new JavaNodeRegion(
                            new CurvesNode(Constants.NODE_CURVES),
                            nodeList,
                            offset,
                            offset + 1
                    );
                case "[":
                    return new JavaNodeRegion(
                            new SquareNode(Constants.NODE_SQUARE),
                            nodeList,
                            offset,
                            offset + 1
                    );
            }
        }
        return null;
    }

    @Override
    protected int onMatchRegionEnd(JavaNodeRegion region, int unclosedIndex, int offset, int maxEnd) {
        Node node = region.getSrc().getNode(offset);
        String type = region.getNode().getType();
        if (node.isToken() && Constants.TOKEN_SYMBOL.equals(node.getType())) {
            Token token = (Token) node;
            if (Constants.NODE_BRACE.equals(type) && "}".equals(token.getText())) {
                return offset + 1;
            } else if (Constants.NODE_CURVES.equals(type) && ")".equals(token.getText())) {
                return offset + 1;
            } else if (Constants.NODE_SQUARE.equals(type) && "]".equals(token.getText())) {
                return offset + 1;
            }
        }
        return REGION_END_NOT_MATCH_AND_CONSUMED;
    }
}
