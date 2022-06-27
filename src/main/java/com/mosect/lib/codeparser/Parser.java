package com.mosect.lib.codeparser;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析器
 */
public class Parser {

    private final List<NodeParser> nodeParsers = new ArrayList<>();

    public List<NodeParser> getNodeParsers() {
        return nodeParsers;
    }

    public void addNodeParser(NodeParser nodeParser) {
        getNodeParsers().add(nodeParser);
    }

    public Node parseDocument(CharSequence text) {
        return parseDocument(text, 0, text.length());
    }

    public Node parseDocument(CharSequence text, int start, int end) {
        List<Node> nodes = parse(text, start, end);
        Group group = new Group();
        group.setType("document");
        group.getChildren().addAll(nodes);
        return group;
    }

    public List<Node> parse(CharSequence text, int start, int end) {
        if (start < 0 || end < start || end > text.length()) {
            throw new IllegalArgumentException(String.format("Invalid text {len=%s,start=%s,end=%s}", text.length(), start, end));
        }
        List<Node> result = new ArrayList<>(64);
        int offset = start;
        NodeInfo out = new NodeInfo();
        while (offset < end) {
            boolean ok = false;
            for (NodeParser np : getNodeParsers()) {
//                System.out.println(offset + "  " + np.getClass());
                /*if (offset == 2716) {
                    System.out.println(offset + "  " + np.getClass());
                }*/
                if (np.parse(text, start, offset, end, out)) {
                    ok = true;
                    break;
                }
            }
            if (ok) {
                result.add(out.node);
                offset = out.offset;
            } else {
                throw new IllegalStateException("No NodeParser can handle text, offset=" + offset);
            }
        }
        return result;
    }
}
