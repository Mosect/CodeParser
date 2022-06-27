package com.mosect.lib.codeparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class IOUtils {

    private IOUtils() {
    }

    private final static ThreadLocal<byte[]> BUFFER = new ThreadLocal<>();

    public static void initParent(File file) throws IOException {
        File parent = file.getParentFile();
        if (null != parent && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Can't create directory: " + parent);
        }
    }

    private static byte[] getBuffer() {
        byte[] buffer = BUFFER.get();
        if (null == buffer) {
            buffer = new byte[1024];
            BUFFER.set(buffer);
        }
        return buffer;
    }

    public static List<String> listFile(File dir, String suffix) {
        List<String> out = new ArrayList<>(12);
        listFiles(dir, suffix, "", out);
        return out;
    }

    private static void listFiles(File dir, String suffix, String prefix, List<String> out) {
        File[] files = dir.listFiles(file -> !".".equals(file.getName()) && !"..".equals(file.getName()));
        if (null != files) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(suffix)) {
                    out.add(prefix + file.getName());
                } else if (file.isDirectory()) {
                    String nextPrefix = prefix + file.getName() + "/";
                    listFiles(file, suffix, nextPrefix, out);
                }
            }
        }
    }

    public static String readText(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream temp = new ByteArrayOutputStream()) {
            int len;
            byte[] buffer = getBuffer();
            while ((len = fis.read(buffer)) >= 0) {
                if (len > 0) temp.write(buffer, 0, len);
            }
            return temp.toString("utf-8");
        }
    }
}
