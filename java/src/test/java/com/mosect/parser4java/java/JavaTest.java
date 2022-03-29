package com.mosect.parser4java.java;

import com.mosect.parser4java.core.source.InputStreamSource;
import com.mosect.parser4java.core.util.NumberUtils;

import org.junit.Assert;
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
    public void testJavaParser() throws Exception {
        changeOutput();
        File file = new File("D:\\Work\\Temp\\java_src\\src\\java.net.http\\java\\net\\http\\HttpClient.java");
        JavaParser javaParser = new JavaParser();
        parseJava(javaParser, file);
    }

    @Test
    public void testParser() throws Exception {
        changeOutput();
        File file = new File("E:\\Temp\\java_src\\src\\java.base");
        List<File> list = new ArrayList<>();
        listJavaFiles(file, list);
        JavaParser javaParser = new JavaParser();
        for (File javaFile : list) {
            parseJava(javaParser, javaFile);
        }
    }

    @Test
    public void testParseHexDecimal() {
        double dv = 0x1.23p+20;
        Number v2 = NumberUtils.parseHexDecimalWithFull64("0x1.23p+20");
        System.out.println(dv);
        System.out.println(v2);
        Assert.assertEquals(dv, v2);
    }

    @Test
    public void testParseHexDecimal32() {
        float fv = 0x1.23p+20f;
        Number v2 = NumberUtils.parseHexDecimalWithFull32("0x1.23p+20");
        System.out.println(fv);
        System.out.println(v2);
        Assert.assertEquals(fv, v2);
    }

    private void parseJava(JavaParser javaParser, File javaFile) throws Exception {
        out.println("parseJava: " + javaFile);
        try (FileInputStream fis = new FileInputStream(javaFile)) {
            InputStreamSource source = new InputStreamSource(fis, "UTF-8");
            javaParser.parse(source, 0);
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
