package com.mosect.lib.codeparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public final class NodeUtils {

    private NodeUtils() {
    }

    public static void check(Node node) throws NodeException {
        if (null != node.getError()) {
            throw new NodeException(String.valueOf(node.getError()));
        }
        if (!node.isToken() && node.getChildCount() > 0) {
            for (Node child : node.getChildren()) {
                check(child);
            }
        }
    }

    public static void write(Node node, File file) throws IOException {
        IOUtils.initParent(file);
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            node.write(osw);
        }
    }
}
