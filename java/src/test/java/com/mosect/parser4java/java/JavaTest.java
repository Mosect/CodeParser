package com.mosect.parser4java.java;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.core.javalike.NumberNodeFactory;
import com.mosect.parser4java.core.javalike.NumberToken;
import com.mosect.parser4java.core.source.InputStreamSource;
import com.mosect.parser4java.core.source.StringSource;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class JavaTest {

    private PrintStream out;

    private void changeOutput() {
        out = System.out;
        File file = new File("build/output.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos, true, "UTF-8");
            System.setOut(ps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNumberParser() throws Exception {
        StringSource source = new StringSource("321L");
        NumberNodeFactory factory = new NumberNodeFactory();
        NumberToken token = factory.parse(null, source, new ArrayList<>());
        if (token.getText().length() != source.length()) {
            throw new IllegalStateException("Invalid number token length");
        }
    }

    @Test
    public void testJavaFile() throws Exception {
        changeOutput();
        File file = new File("D:\\Work\\Temp\\java_src\\src\\java.net.http\\java\\net\\http\\HttpHeaders.java");
        JavaFactory javaFactory = new JavaFactory();
        parseJavaFile(javaFactory, file);
    }

    @Test
    public void testJavaParser() throws Exception {
        changeOutput();
        File file = new File("D:\\Work\\Temp\\java_src\\src\\java.net.http\\java\\net\\http\\HttpClient.java");
        JavaParser javaParser = new JavaParser();
        parseJava(javaParser, file);
    }

    @Test
    public void testParser() throws Exception {
        changeOutput();
        File file = new File("D:\\Work\\Temp\\java_src\\src\\java.base");
        List<File> list = new ArrayList<>();
        listJavaFiles(file, list);
        JavaParser javaParser = new JavaParser();
        for (File javaFile : list) {
            parseJava(javaParser, javaFile);
        }
    }

    private void parseJavaFile(JavaFactory javaFactory, File javaFile) throws Exception {
        out.println("javaFile: " + javaFile);
        try (FileInputStream fis = new FileInputStream(javaFile)) {
            InputStreamSource source = new InputStreamSource(fis, "UTF-8");
            List<ParseError> outErrors = new ArrayList<>();
            Node node = javaFactory.parse(source, outErrors);
            if (outErrors.size() > 0) {
                for (ParseError error : outErrors) {
                    System.err.println(source.splitLine(error.getLinePosition()));
                    System.err.println(error);
                }
                throw new IllegalStateException("InvalidJavaFile: " + javaFile);
            }
            printNode(node, source);
        }
    }

    private void parseJava(JavaParser javaParser, File javaFile) throws Exception {
        out.println("parseJava: " + javaFile);
        try (FileInputStream fis = new FileInputStream(javaFile)) {
            InputStreamSource source = new InputStreamSource(fis, "UTF-8");
            javaParser.parse(source, 0);
        }
    }

    private void printNode(Node node, TextSource source) {
        if (node.isToken()) {
            Token token = (Token) node;
            switch (token.getType()) {
                case "char":
//                    System.out.println("char: " + token.getText());
                    break;
                case "string":
//                    System.out.println("string: " + token.getText());
                    break;
                case "linefeed":
//                    System.out.println("linefeed: " + token.getName());
                    break;
                case "whitespace":
//                    System.out.println("whitespace: " + token.getName());
                    break;
                case "number":
                    System.out.println("number: " + token.getText());
                    break;
                case "symbol":
                    break;
                case "name":
//                    System.out.println("name: " + token.getText());
                    break;
                case "comment":
//                    System.out.println("COMMENT=================================");
//                    System.out.println(token.getText());
                    break;
                case "keyword":
//                    System.out.println("keyword: " + token.getText());
                    break;
                default:
                    System.out.println("unknownType: " + token.getType());
                    break;
            }
        } else {
            if (node.getChildCount() > 0) {
                for (Node child : node) {
                    printNode(child, source);
                }
            }
        }
    }

    private void listJavaFiles(File file, List<File> outList) {
        if (file.isFile()) {
            if (file.getName().endsWith(".java")) {
                outList.add(file);
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File child : files) {
                    String name = child.getName();
                    if (".".equals(name) || "..".equals(name)) continue;
                    listJavaFiles(child, outList);
                }
            }
        }
    }
}
