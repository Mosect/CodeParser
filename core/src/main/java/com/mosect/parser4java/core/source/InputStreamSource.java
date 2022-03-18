package com.mosect.parser4java.core.source;

import com.mosect.parser4java.core.TextSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class InputStreamSource extends TextSource {

    private final StringBuilder text;

    public InputStreamSource(InputStream ins, String charset) throws IOException {
        text = new StringBuilder(512);
        try (InputStreamReader reader = new InputStreamReader(ins, charset)) {
            char[] buffer = new char[128];
            int len;
            while ((len = reader.read(buffer)) >= 0) {
                if (len > 0) {
                    text.append(buffer, 0, len);
                }
            }
        }
    }

    public InputStreamSource(InputStream ins) throws IOException {
        this(ins, Charset.defaultCharset().name());
    }

    @Override
    public String getName() {
        return "InputStream";
    }

    @Override
    public CharSequence getText() {
        return text;
    }
}
