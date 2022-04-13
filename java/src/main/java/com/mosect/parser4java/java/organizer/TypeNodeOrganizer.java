package com.mosect.parser4java.java.organizer;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeList;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;

/**
 * 类型节点解析器
 */
public class TypeNodeOrganizer extends JavaNodeOrganizer {

    @Override
    protected JavaNodeRegion onMatchRegion(NodeList nodeList, Node node, int start, int offset) {
        if (node.isToken()) {
            Token token = (Token) node;
            if (Constants.TOKEN_KEYWORD.equals(token.getType())) {
                switch (token.getText()) {
                    case "class":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_CLASS, nodeList, start, offset);
                    case "interface":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_INTERFACE, nodeList, start, offset);
                    case "@interface":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_ANNOTATION, nodeList, start, offset);
                    case "enum":
                        return createTypeNodeRegion(Constants.CLASS_TYPE_ENUM, nodeList, start, offset);
                    case "package":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_PACKAGE, nodeList, offset);
                    case "import":
                        return createKeywordSentence(Constants.SENTENCE_TYPE_IMPORT, nodeList, offset);
                }
            } else if (Constants.TOKEN_SYMBOL.equals(token.getType())) {
                if (";".equals(token.getText())) { // 未知语句
                    return createUnknownSentence(nodeList, start, offset);
                }
            }
        } else {
            switch (node.getType()) {
                case Constants.NODE_CURVES: // 圆括号
            }
        }
        return null;
    }
}
