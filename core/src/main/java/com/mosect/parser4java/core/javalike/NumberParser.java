package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.util.CharUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数字文本解析器
 */
public class NumberParser extends CommonTextParser {

    private String prefix;
    private String suffix;
    private Number value;
    private boolean integer; // 是否为整数
    private int radix; // 进制
    private final StringBuilder numberBuilder;
    private RadixCharHandler hexCharHandler;
    private RadixCharHandler binCharHandler;
    private RadixCharHandler octCharHandler;
    private RadixCharHandler decCharHandler;

    public NumberParser() {
        numberBuilder = new StringBuilder(64);
        setHexCharHandler(CharUtils::isHexChar);
        setBinCharHandler(CharUtils::isBinChar);
        setOctCharHandler(CharUtils::isOctChar);
        setDecCharHandler(CharUtils::isDecChar);
    }

    @Override
    protected void onClear() {
        super.onClear();
        StringBuilder numberBuilder = getNumberBuilder();
        if (numberBuilder.length() > 0) {
            numberBuilder.delete(0, numberBuilder.length());
        }
        value = null;
        setInteger(true); // 默认整数
        setRadix(10); // 默认十进制
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        boolean radix10 = false; // 进行十进制解析
        int end = start;
        boolean skipInteger = false; // 跳过整数部分
        boolean parseMantissa = false; // 解析尾数
        String name = null;
        StringBuilder numberBuilder = getNumberBuilder();
        if (CharUtils.match(text, start, "0x", true)) {
            setPrefix("0x");
            // 十六进制
            int offset = start + 2;
            setRadix(16);
            name = "NUMBER_HEX";

            if (CharUtils.match(text, end, ".", false)) {
                // 0x.123
                name = "NUMBER_DEC_HEX";
                setInteger(false);
                ++end;
                
            }
            end = parseNumber(name, text, offset, getHexCharHandler());
            if (end == offset) {
                putError(name + "_EMPTY_CONTENT", "Empty hex content", offset);
            } else {
                if (CharUtils.match(text, end, ".", false)) {
                    // 十六进制小数，样式：0x1.xxxp-xxx
                    ++end;
                    numberBuilder.append('.');
                    setInteger(false);
                    name = "NUMBER_DEC_HEX";
                    offset = end;
                    end = parseNumber(name, text, end, getHexCharHandler());
                }

                if (CharUtils.match(text, end, "p", true)) {
                    setInteger(false);
                    name = "NUMBER_DEC_HEX";
                    // 含有p字母
                    ++end;
                    numberBuilder.append('p');
                    end = parsePower(name, text, end);
                } else {
                    putError(name + "_MISSING_POWER", "Missing char: p", end);
                }
            }
        } else if (CharUtils.match(text, start, "0b", true)) {
            setPrefix("0b");
            // 二进制
            setRadix(2);
            int offset = start + 2;
            name = "NUMBER_BIN";
            end = parseNumber(name, text, offset, getBinCharHandler());
            if (end == offset) {
                putError(name + "_EMPTY_CONTENT", "Empty bin content", offset);
            }
        } else if (CharUtils.match(text, start, "0", false)) {
            // 0开头，可能为0，或者为八进制
            int offset = start + 1;
            setRadix(8);
            name = "NUMBER_OCT";
            end = parseNumber(name, text, offset, getBinCharHandler());
            if (end == offset) {
                // 非八进制
                radix10 = true;
            }
        } else if (CharUtils.match(text, start, ".", false)) {
            int nextIndex = start + 1;
            if (nextIndex < text.length() && CharUtils.isDecChar(text.charAt(nextIndex))) {
                // 点开头，例子：.123d
                setInteger(false);
                ++end;
                skipInteger = true; // 跳过整数部分
                radix10 = true;
                parseMantissa = true;
                name = "NUMBER_DEC";
                numberBuilder.append("0.");
            } else {
                finishParse(false, start);
                return;
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
            name = "NUMBER_DEC";
            int offset = start;
            if (!skipInteger) {
                end = parseNumber(name, text, start, getDecCharHandler());
                if (end == offset) {
                    // 无法解析的内容
                    finishParse(false, start);
                    return;
                }
                if (CharUtils.match(text, end, ".", false)) {
                    numberBuilder.append('.');
                    setInteger(false);
                    parseMantissa = true;
                    ++end;
                }
            }
            if (parseMantissa) {
                // 解析尾数
                offset = end;
                end = parseNumber(name, text, end, getDecCharHandler());
                if (end != offset) {
                    // 有尾数部分
                    if (CharUtils.match(text, end, "e", true)) {
                        // 科学计数法，解析次方
                        numberBuilder.append('e');
                        end = parsePower(name, text, end);
                    }
                } else {
                    // 缺少尾数，合法
                }
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
                        if (!isInteger()) {
                            // 小数，不支持此后缀
                            putError(name + "_UNSUPPORTED_SUFFIX", "Unsupported suffix: " + endChar, end);
                        }
                        setSuffix("L");
                        ++end;
                        break;
                }
            }
        }

        finishParse(true, end);
    }

    /**
     * 解析次方
     *
     * @param name   名称
     * @param text   文本
     * @param offset 偏移量
     * @return 解析的位置
     */
    protected int parsePower(String name, CharSequence text, int offset) {
        int end = offset;
        StringBuilder numberBuilder = getNumberBuilder();
        if (CharUtils.match(text, end, "+", false)) {
            // 正号
            numberBuilder.append('+');
            ++end;
        } else if (CharUtils.match(text, end, "-", false)) {
            // 负号
            numberBuilder.append('-');
            ++end;
        }
        int offset2 = end;
        end = parseNumber(name, text, end, getDecCharHandler());
        if (end == offset2) {
            // 缺少 次方 部分
            putError(name + "_MISSING_POWER", "Missing power", end);
        }
        return end;
    }


    /**
     * 解析数值
     *
     * @param name        名称
     * @param text        文本
     * @param offset      偏移量
     * @param charHandler 字符处理器
     * @return 解析结束位置
     */
    protected int parseNumber(String name, CharSequence text, int offset, RadixCharHandler charHandler) {
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
                    putError(name + "_UNDERLINE_END", "Not supported underline end", i);
                    break;
                } else if (numberBuilder.length() == 0) {
                    // 不允许开始有下划线
                    putError(name + "_UNDERLINE_START", "Not supported underline start", i);
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
                try {
                    if (isInteger()) {
                        // 整数
                        value = new BigInteger(str, getRadix());
                    } else {
                        // 小数
                        if (getRadix() == 16) {
                            // 十六进制小数
                            int pointIndex = str.indexOf('.');
                            int powerIndex = str.indexOf('p');
                            String firstStr, endStr;
                            if (pointIndex > 0) {
                                firstStr = str.substring(2, pointIndex);
                                endStr = str.substring(pointIndex + 1, powerIndex);
                            } else {
                                firstStr = str.substring(2, powerIndex);
                                endStr = "";
                            }
                            long firstNum = firstStr.length() > 0 ? Long.parseLong(firstStr, 16) : 0;
                            long endNum = endStr.length() > 0 ? Long.parseLong(endStr, 16) : 0;
                            String numStr = firstNum + "." + endNum;
                            String powerStr = str.substring(powerIndex + 1);
                            int power = Integer.parseInt(powerStr);
                            value = new BigDecimal(numStr).multiply(new BigDecimal("2").pow(power));
                        } else {
                            value = new BigDecimal(str);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("InvalidNumber: " + str);
                    throw e;
                }
            }
            return value;
        }
        return null;
    }

    /**
     * 判断是否为整数
     *
     * @return true，整数；false，非整数
     */
    public boolean isInteger() {
        return integer;
    }

    /**
     * 设置是否为整数
     *
     * @param integer 是否为整数
     */
    protected void setInteger(boolean integer) {
        this.integer = integer;
    }

    /**
     * 获取进制数
     *
     * @return 进制
     */
    public int getRadix() {
        return radix;
    }

    /**
     * 设置进制
     *
     * @param radix 进制
     */
    protected void setRadix(int radix) {
        this.radix = radix;
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
        boolean isValidChar(char ch);
    }
}
