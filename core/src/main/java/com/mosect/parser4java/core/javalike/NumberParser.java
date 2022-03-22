package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.util.CharUtils;

import java.math.BigInteger;

/**
 * 数字文本解析器
 */
public class NumberParser extends CommonTextParser {

    private String prefix;
    private String suffix;
    private Number value;
    private final StringBuilder numberBuilder;
    private RadixCharHandler hexCharHandler;
    private RadixCharHandler binCharHandler;
    private RadixCharHandler octCharHandler;
    private RadixCharHandler decCharHandler;

    public NumberParser() {
        numberBuilder = new StringBuilder(64);
        setHexCharHandler(new RadixCharHandler() {
            @Override
            public String getName() {
                return "NUMBER_HEX";
            }

            @Override
            public boolean isValidChar(char ch) {
                return CharUtils.isHexChar(ch);
            }
        });
        setBinCharHandler(new RadixCharHandler() {
            @Override
            public String getName() {
                return "NUMBER_BIN";
            }

            @Override
            public boolean isValidChar(char ch) {
                return CharUtils.isBinChar(ch);
            }
        });
        setOctCharHandler(new RadixCharHandler() {
            @Override
            public String getName() {
                return "NUMBER_OCT";
            }

            @Override
            public boolean isValidChar(char ch) {
                return CharUtils.isOctChar(ch);
            }
        });
        setDecCharHandler(new RadixCharHandler() {
            @Override
            public String getName() {
                return "NUMBER_DEC";
            }

            @Override
            public boolean isValidChar(char ch) {
                return CharUtils.isDecChar(ch);
            }
        });
    }

    @Override
    protected void onClear() {
        super.onClear();
        StringBuilder numberBuilder = getNumberBuilder();
        if (numberBuilder.length() > 0) {
            numberBuilder.delete(0, numberBuilder.length());
        }
        value = null;
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        boolean radix10 = false; // 进行十进制解析
        int end = start;
        if (CharUtils.match(text, start, "0x", true)) {
            setPrefix("0x");
            // 十六进制
            int offset = start + 2;
            end = parseNumber(text, offset, getHexCharHandler());
            if (end == offset) {
                putError(getHexCharHandler().getName() + "_EMPTY_CONTENT", "Empty hex content", offset);
            }
        } else if (CharUtils.match(text, start, "0b", true)) {
            setPrefix("0b");
            // 二进制
            int offset = start + 2;
            end = parseNumber(text, offset, getBinCharHandler());
            if (end == offset) {
                putError(getBinCharHandler().getName() + "_EMPTY_CONTENT", "Empty bin content", offset);
            }
        } else if (CharUtils.match(text, start, "0", false)) {
            // 0开头，可能为0，或者为八进制
            int offset = start + 1;
            end = parseNumber(text, offset, getBinCharHandler());
            if (end == offset) {
                // 非八进制
                radix10 = true;
            }
        } else if (text.length() > start && CharUtils.isDecChar(text.charAt(start))) {
            // 十进制
            radix10 = true;
        } else {
            finishParse(false, start);
            return;
        }

        if (radix10) {
            // 进行十进制解析
            end = parseNumber(text, start, getDecCharHandler());
            if (end == start) {
                // 无法解析的内容
                finishParse(false, start);
                return;
            }
        }

        // 处理后缀
        if (!hasError()) {
            if (end < text.length()) {
                char endChar = text.charAt(end);
                switch (endChar) {
                    case 'D':
                    case 'd':
                        setSuffix("D");
                        ++end;
                        break;
                    case 'F':
                    case 'f':
                        setSuffix("F");
                        ++end;
                        break;
                    case 'L':
                    case 'l':
                        setSuffix("L");
                        ++end;
                        break;
                    case 'P':
                    case 'p':
                        setSuffix("P");
                        ++end;
                        break;
                }
            }
        }

        finishParse(true, end);
    }

    /**
     * 解析数值
     *
     * @param text        文本
     * @param offset      偏移量
     * @param charHandler 字符处理器
     * @return 解析结束位置
     */
    protected int parseNumber(CharSequence text, int offset, RadixCharHandler charHandler) {
        StringBuilder numberBuilder = getNumberBuilder();
        int end = offset;
        for (int i = offset; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (charHandler.isValidChar(ch)) {
                numberBuilder.append(ch);
                ++end;
            } else if (ch == '_') {
                // 允许下划线
                ++end;
                if (i == text.length() - 1) {
                    // 不允许结束有下划线
                    putError(charHandler.getName() + "_UNDERLINE_END", "Not supported underline end", i);
                    break;
                } else if (numberBuilder.length() == 0) {
                    // 不允许开始有下划线
                    putError(charHandler.getName() + "_UNDERLINE_START", "Not supported underline start", i);
                    break;
                }
            } else {
                // 其他非法字符
                break;
            }
        }
        return end;
    }

    /**
     * 获取前缀
     *
     * @return 前缀
     */
    public String getPrefix() {
        return prefix;
    }

    protected void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 获取后缀
     *
     * @return 后缀
     */
    public String getSuffix() {
        return suffix;
    }

    protected void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * 获取解析的值
     *
     * @return 解析的数值
     */
    public Number getValue() {
        if (isSuccess()) {
            if (null == value) {
                String str = getNumberBuilder().toString();
                value = new BigInteger(str);
            }
            return value;
        }
        return null;
    }

    protected StringBuilder getNumberBuilder() {
        return numberBuilder;
    }

    public RadixCharHandler getHexCharHandler() {
        return hexCharHandler;
    }

    public void setHexCharHandler(RadixCharHandler hexCharHandler) {
        this.hexCharHandler = hexCharHandler;
    }

    public RadixCharHandler getBinCharHandler() {
        return binCharHandler;
    }

    public void setBinCharHandler(RadixCharHandler binCharHandler) {
        this.binCharHandler = binCharHandler;
    }

    public RadixCharHandler getOctCharHandler() {
        return octCharHandler;
    }

    public void setOctCharHandler(RadixCharHandler octCharHandler) {
        this.octCharHandler = octCharHandler;
    }

    public RadixCharHandler getDecCharHandler() {
        return decCharHandler;
    }

    public void setDecCharHandler(RadixCharHandler decCharHandler) {
        this.decCharHandler = decCharHandler;
    }

    @Override
    public String getName() {
        return "java.number";
    }

    protected interface RadixCharHandler {
        String getName();

        boolean isValidChar(char ch);
    }
}
