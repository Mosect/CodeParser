package com.mosect.parser4java.core.text;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.util.CharUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 字符解析器
 */
public class CharTextParser extends CommonTextParser {

    private final Map<String, EscapeChar> escapeCharMap = new HashMap<>();
    private boolean supportedEscapeHexUnicode; // 是否支持转义十六进制的u码
    private boolean supportedEscapeOctUnicode; // 是否支持转义八进制的u码
    private char value;

    public CharTextParser() {
        setSupportedEscapeHexUnicode(true);
        setSupportedEscapeOctUnicode(true);
        registerEscapeChar("\\r", '\r');
        registerEscapeChar("\\n", '\n');
        registerEscapeChar("\\t", '\t');
        registerEscapeChar("\\b", '\b');
        registerEscapeChar("\\f", '\f');
        registerEscapeChar("\\\\", '\\');
        registerEscapeChar("\\\"", '"');
        registerEscapeChar("\\'", '\\');
    }

    /**
     * 注册转义字符
     *
     * @param text  转义文本
     * @param value 转义值
     */
    public void registerEscapeChar(String text, char value) {
        registerEscapeChar(new EscapeChar(text, value));
    }

    /**
     * 注册转义字符
     *
     * @param escapeChar 转义字符
     */
    public void registerEscapeChar(EscapeChar escapeChar) {
        escapeCharMap.put(escapeChar.getText(), escapeChar);
    }

    /**
     * 注销转义字符
     *
     * @param text 转义字符串
     */
    public void unregisterEscapeChar(String text) {
        escapeCharMap.remove(text);
    }

    /**
     * 所有支持的转义字符
     *
     * @return 转义字符
     */
    public Collection<EscapeChar> allEscapeChars() {
        return escapeCharMap.values();
    }

    /**
     * 判断是否支持转义的十六进制u码
     *
     * @return true，支持；false，不支持
     */
    public boolean isSupportedEscapeHexUnicode() {
        return supportedEscapeHexUnicode;
    }

    /**
     * 设置是否支持转义的十六进制u码
     *
     * @param supportedEscapeHexUnicode true，支持；false，不支持
     */
    public void setSupportedEscapeHexUnicode(boolean supportedEscapeHexUnicode) {
        this.supportedEscapeHexUnicode = supportedEscapeHexUnicode;
    }

    /**
     * 判断是否支持转义的八进制u码
     *
     * @return true，支持；false，不支持
     */
    public boolean isSupportedEscapeOctUnicode() {
        return supportedEscapeOctUnicode;
    }

    /**
     * 设置是否支持转义的八进制u码
     *
     * @param supportedEscapeOctUnicode true，支持；false，不支持
     */
    public void setSupportedEscapeOctUnicode(boolean supportedEscapeOctUnicode) {
        this.supportedEscapeOctUnicode = supportedEscapeOctUnicode;
    }

    /**
     * 获取解析的值<br>
     * {@link #isSuccess()}为true，此值才有效
     *
     * @return 解析的字符
     */
    public char getValue() {
        return value;
    }

    /**
     * 设置解析的字符
     *
     * @param value 字符值
     */
    protected void setValue(char value) {
        this.value = value;
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        // 处理转义字符串
        for (EscapeChar ec : escapeCharMap.values()) {
            if (CharUtils.match(text, start, ec.getText(), false)) {
                // 符合转义字符
                setValue(ec.getValue());
                finishParse(true, start + ec.getText().length());
                return;
            }
        }

        // 处理十六进制字符
        if (isSupportedEscapeHexUnicode()) {
            // 支持十六进制u码
            if (CharUtils.match(text, start, "\\u", false)) {
                int offset = start + 2;
                int size = 0;
                for (int i = offset; i < text.length(); i++) {
                    char ch = text.charAt(i);
                    if (CharUtils.isHexChar(ch)) {
                        if (++size == 4) break;
                    } else {
                        break;
                    }
                }
                finishParse(true, offset + size);
                if (size == 4) {
                    char value = (char) Integer.parseInt(text.subSequence(offset, offset + size).toString(), 16);
                    setValue(value);
                } else {
                    putError("HEX_CHAR_INVALID_END", "Hex char not like \\uxxxx", offset + size);
                }
                return;
            }
        }

        // 处理八进制u码
        if (isSupportedEscapeOctUnicode()) {
            if (CharUtils.match(text, start, "\\", false)) {
                int size = 0;
                int offset = start + 1;
                for (int i = offset; i < text.length(); i++) {
                    char ch = text.charAt(i);
                    if (CharUtils.isOctChar(ch)) {
                        if (++size == 3) break;
                    } else {
                        break;
                    }
                }
                if (size > 0) {
                    finishParse(true, offset + size);
                    char value = (char) Integer.parseInt(text.subSequence(offset, offset + size).toString(), 8);
                    setValue(value);
                    return;
                }
            }
        }

        if (text.length() > start) {
            setValue(text.charAt(start));
            finishParse(true, start + 1);
            return;
        }

        finishParse(false, start);
    }

    @Override
    public String getName() {
        return "common.char";
    }

    /**
     * 转义符
     */
    public static class EscapeChar {
        private final String text;
        private final char value;

        public EscapeChar(String text, char value) {
            this.text = text;
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public char getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "EscapeChar{" +
                    "text='" + text + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
}
