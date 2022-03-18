package com.mosect.parser4java.core.source;

import com.mosect.parser4java.core.TextSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileSource extends TextSource {

    private final CharSequence text;
    private final File file;

    public FileSource(File file, String charset) throws IOException {
        this.file = file;
        try (FileInputStream fis = new FileInputStream(file)) {
            text = new InputStreamSource(fis, charset).getText();
        }
    }

    public FileSource(File file) throws IOException {
        this(file, Charset.defaultCharset().name());
    }

    public File getFile() {
        return file;
    }

    @Override
    public String getName() {
        return String.format("File{%s}", file);
    }

    @Override
    public CharSequence getText() {
        return text;
    }
}
