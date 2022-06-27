package com.mosect.lib.codeparser;

import com.mosect.lib.codeparser.java.JavaGroupParser;
import com.mosect.lib.codeparser.java.JavaParser;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JavaTest {

    private final JavaParser javaParser = new JavaParser();
    private final JavaGroupParser groupParser = new JavaGroupParser();

    @Test
    public void testJavaFile() throws Exception {
//        Node node = parseJavaFile(new File("E:\\Temp\\jdk1.8.0_251\\src\\com\\sun\\corba\\se\\impl\\activation\\RepositoryImpl.java"));
        Node node = parseJavaFile(new File("E:\\Temp\\jdk1.8.0_251\\src\\com\\sun\\java\\swing\\plaf\\windows\\WindowsGraphicsUtils.java"));
    }

    @Test
    public void testSrc() throws Exception {
        File dir = new File("E:\\Temp\\jdk1.8.0_251\\src");
        List<String> paths = IOUtils.listFile(dir, ".java");
        File outDir = new File("build/test/src");
        for (String path : paths) {
            Node node = parseJavaFile(new File(dir, path));
            File outFile = new File(outDir, path);
            NodeUtils.write(node, outFile);
        }
    }

    private Node parseJavaFile(File file) throws IOException, NodeException {
        System.out.println("parseJavaFile: " + file);
        String text = IOUtils.readText(file);
        Node node = javaParser.parseDocument(text);
        groupParser.adjust(node);
        /*for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChildren().get(i);
            if ("char".equals(child.getType())) {
                System.out.println("=======================================================================");
                child.write(System.out);
                System.out.println();
            }
        }*/
        NodeUtils.check(node);
        return node;
    }
}
