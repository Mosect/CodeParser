package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.util.CharUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 数字节点工厂
 */
public class NumberNodeFactory implements NodeFactory {

    protected StringBuilder numberTemp = new StringBuilder(32);

    @Override
    public String getName() {
        return "number";
    }

    @Override
    public NumberToken parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        if (source.hasMore()) {
            CharSequence text = source.getText();
            int pos = source.getPosition();
            boolean negative = false;
            int offset = 0;
            if (source.match("-")) {
                negative = true;
                ++offset;
            } else if (source.match("+")) {
                ++offset;
            }
            if (source.match(offset, "0x1.") || source.match(offset, "0X1.")) {
                // 十六进制小数
                offset += 4;
                // 解析十六进制数
                int hexCharCount = parseHexNumber(text, pos + offset, numberTemp);
                if (hexCharCount > 0) {
                    // 解析十六进制成功
                    offset += hexCharCount;
                    if (source.match(offset, "p") || source.match(offset, "P")) {
                        numberTemp.append('p');
                        ++offset;
                        if (source.match(offset, "+")) {
                            numberTemp.append('+');
                            ++offset;
                        } else if (source.match(offset, "-")) {
                            ++offset;
                            numberTemp.append("-");
                        }
                        int decCharCount = parseDecNumber(text, pos + offset, numberTemp);
                        if (decCharCount > 0) {
                            offset += decCharCount;
                            NumberType type = NumberType.DOUBLE;
                            // 处理后缀
                            if (source.match(offset, "d") || source.match(offset, "D")) {
                                type = NumberType.DOUBLE;
                                ++offset;
                            } else if (source.match(offset, "f") || source.match(offset, "F")) {
                                type = NumberType.FLOAT;
                                ++offset;
                            }
                            String str = text.subSequence(pos, pos + offset).toString();
                            String name = negative ? "negative_hex_decimal" : "hex_decimal";
                            Number value = new BigInteger(numberTemp.toString());
                            source.offset(offset);
                            return createNumberType(name, str, type, value);
                        } else {
                            throw new ParseException("INVALID_HEX_DECIMAL_END", pos + offset);
                        }
                    } else {
                        throw new ParseException("INVALID_HEX_DECIMAL_MISSING_P", pos + offset);
                    }
                } else {
                    // 解析十六进制失败
                    throw new ParseException("INVALID_HEX_DECIMAL", pos + offset);
                }
            } else if (source.match(offset, "0x") || source.match(offset, "0X")) {
                // 十六进制
                offset += 2;
                clearNumberTemp();
                // 解析十六进制数
                int hexCharCount = parseHexNumber(text, pos + offset, numberTemp);
                if (hexCharCount > 0) {
                    // 解析十六进制成功
                    NumberType type = NumberType.INT;
                    offset += hexCharCount;
                    if (source.match(offset, "l") || source.match(offset, "L")) {
                        type = NumberType.LONG;
                        ++offset;
                    }
                    String str = text.subSequence(pos, pos + offset).toString();
                    String name = negative ? "negative_hex" : "hex";
                    Number value = new BigInteger(numberTemp.toString(), 16);
                    source.offset(offset);
                    return createNumberType(name, str, type, value);
                } else {
                    // 解析十六进制失败
                    throw new ParseException("INVALID_HEX_NUMBER", pos + offset);
                }
            } else if (source.match(offset, "0b") || source.match(offset, "0B")) {
                // 二进制
                offset += 2;
                clearNumberTemp();
                int binCharCount = parseBinNumber(text, pos + offset, numberTemp);
                if (binCharCount > 0) {
                    // 解析十六进制成功
                    NumberType type = NumberType.INT;
                    offset += binCharCount;
                    if (source.match(offset, "l") || source.match(offset, "L")) {
                        type = NumberType.LONG;
                        ++offset;
                    }
                    String str = text.subSequence(pos, pos + offset).toString();
                    String name = negative ? "negative_bin" : "bin";
                    Number value = new BigInteger(numberTemp.toString(), 2);
                    source.offset(offset);
                    return createNumberType(name, str, type, value);
                } else {
                    // 解析二进制失败
                    throw new ParseException("INVALID_BIN_NUMBER", pos + offset);
                }
            } else if (source.match(offset, "0.")) {
                // 小数
                return parseDecimal(source, offset, negative);
            } else if (source.match(offset, "0")) {
                // 八进制或者0
                ++offset;
                clearNumberTemp();
                int octCharCount = parseOctNumber(text, pos + offset, numberTemp);
                NumberType type = NumberType.INT;
                offset += octCharCount;
                if (source.match(offset, "l") || source.match(offset, "L")) {
                    type = NumberType.LONG;
                    ++offset;
                } else if (source.match(offset, "d") || source.match(offset, "D")) {
                    type = NumberType.DOUBLE;
                    ++offset;
                } else if (source.match(offset, "f") || source.match(offset, "F")) {
                    type = NumberType.FLOAT;
                    ++offset;
                }

                String name;
                Number value;
                if (octCharCount > 0) {
                    // 八进制数
                    if (type != NumberType.INT && type != NumberType.LONG) {
                        throw new ParseException("INVALID_OCT_NUMBER_TYPE", pos + offset);
                    }
                    name = negative ? "negative_oct" : "oct";
                    value = new BigInteger(numberTemp.toString(), 8);
                } else {
                    name = negative ? "negative_zero" : "zero";
                    value = 0;
                }

                String str = text.subSequence(pos, pos + offset).toString();
                source.offset(offset);
                return createNumberType(name, str, type, value);
            } else {
                // 十进制
                return parseDecimal(source, offset, negative);
            }
        }
        return null;
    }

    protected NumberToken createNumberType(String name, String text, NumberType type, Number value) {
        return new NumberToken(name, text, type, value);
    }

    protected void clearNumberTemp() {
        if (numberTemp.length() > 0) {
            numberTemp.delete(0, numberTemp.length());
        }
    }

    /**
     * 解析十进制数字
     *
     * @param source   文本源
     * @param offset   偏移量
     * @param negative 是否为负数
     * @return 数字token
     * @throws ParseException 解析异常
     */
    protected NumberToken parseDecimal(TextSource source, int offset, boolean negative) throws ParseException {
        clearNumberTemp();
        int pos = source.getPosition();
        CharSequence text = source.getText();
        int decCharCount = parseDecNumber(text, pos + offset, numberTemp);
        if (decCharCount > 0) {
            // 解析到十进制
            NumberType type = NumberType.INT;
            boolean pow = false;

            offset += decCharCount;
            if (source.match(offset, ".")) {
                // 小数点
                ++offset;
                type = NumberType.DOUBLE;
                numberTemp.append('.');
                decCharCount = parseDecNumber(text, pos + offset, numberTemp);
                if (decCharCount <= 0) {
                    throw new ParseException("INVALID_DECIMAL_EMPTY_AFTER_POINT", pos + offset);
                }
                offset += decCharCount;
            }

            if (source.match(offset, "e") || source.match(offset, "E")) {
                // 科学计数法
                pow = true;
                numberTemp.append("E");
                ++offset;
                // 解析幂
                decCharCount = parseDecNumber(text, pos + offset, numberTemp);
                if (decCharCount <= 0) {
                    throw new ParseException("INVALID_DECIMAL_POW", pos + offset);
                }
                offset += decCharCount;
            }

            if (type == NumberType.INT) {
                if (source.match(offset, "l") || source.match(offset, "L")) {
                    type = NumberType.LONG;
                    ++offset;
                }
            }
            if (source.match(offset, "d") || source.match(offset, "D")) {
//                    type = NumberType.DOUBLE;
                ++offset;
            } else if (source.match(offset, "f") || source.match(offset, "F")) {
                type = NumberType.LONG;
                ++offset;
            }

            String name;
            if (negative) {
                if (pow) {
                    name = "negative_decimal_pow";
                } else {
                    name = "negative_decimal";
                }
            } else {
                if (pow) {
                    name = "decimal_pow";
                } else {
                    name = "decimal";
                }
            }

            String str = text.subSequence(pos, pos + offset).toString();
            BigDecimal value = new BigDecimal(numberTemp.toString());
            source.setPosition(pos + offset);
            return createNumberType(name, str, type, value);
        }
        return null;
    }

    /**
     * 解析二进制数字
     *
     * @param text     文本
     * @param position 偏移量
     * @param out      输出的数字字符
     * @return 解析所用的字符数量
     * @throws ParseException 解析异常
     */
    protected int parseBinNumber(CharSequence text, int position, StringBuilder out) throws ParseException {
        int curOffset = position;
        int charCount = 0;
        boolean underline = false;
        while (curOffset < text.length()) {
            char ch = text.charAt(curOffset);
            if (CharUtils.isBinChar(ch)) {
                out.append(ch);
                ++charCount;
                underline = false;
            } else if (ch == '_') {
                // java7 语法，支持下划线
                if (charCount == 0) {
                    // 不能开头下划线
                    throw new ParseException("INVALID_BIN_NUMBER_START", curOffset);
                }
                underline = true;
            } else {
                // 其他无法识别的字符
                break;
            }
            ++curOffset;
        }
        if (underline) {
            // 不能以下划线结尾
            throw new ParseException("INVALID_BIN_NUMBER_END", curOffset - 1);
        }
        return curOffset - position;
    }

    /**
     * 解析十六进制数字
     *
     * @param text     文本
     * @param position 偏移量
     * @param out      输出的数字字符
     * @return 解析所用的字符数量
     * @throws ParseException 解析异常
     */
    protected int parseHexNumber(CharSequence text, int position, StringBuilder out) throws ParseException {
        int curOffset = position;
        int charCount = 0;
        boolean underline = false;
        while (curOffset < text.length()) {
            char ch = text.charAt(curOffset);
            if (CharUtils.isHexChar(ch)) {
                out.append(ch);
                ++charCount;
                underline = false;
            } else if (ch == '_') {
                // java7 语法，支持下划线
                if (charCount == 0) {
                    // 不能开头下划线
                    throw new ParseException("INVALID_HEX_NUMBER_START", curOffset);
                }
                underline = true;
            } else {
                // 其他无法识别的字符
                break;
            }
            ++curOffset;
        }
        if (underline) {
            // 不能以下划线结尾
            throw new ParseException("INVALID_HEX_NUMBER_END", curOffset - 1);
        }
        return curOffset - position;
    }

    /**
     * 解析八进制数
     *
     * @param text     文本
     * @param position 位置
     * @param out      输出的数字字符
     * @return 解析所用的字符数量
     * @throws ParseException 解析异常
     */
    protected int parseOctNumber(CharSequence text, int position, StringBuilder out) throws ParseException {
        int curOffset = position;
        boolean underline = false;
        while (curOffset < text.length()) {
            char ch = text.charAt(curOffset);
            if (CharUtils.isOctChar(ch)) {
                out.append(ch);
                underline = false;
            } else if (ch == '_') {
                // java7 语法，支持下划线
                underline = true;
            } else {
                // 其他无法识别的字符
                break;
            }
            ++curOffset;
        }
        if (underline) {
            // 不能以下划线结尾
            throw new ParseException("INVALID_OCT_NUMBER_END", curOffset - 1);
        }
        return curOffset - position;
    }

    /**
     * 解析十进制数
     *
     * @param text     文本
     * @param position 位置
     * @param out      输出的数字字符
     * @return 解析所用的字符数量
     * @throws ParseException 解析异常
     */
    protected int parseDecNumber(CharSequence text, int position, StringBuilder out) throws ParseException {
        int curOffset = position;
        int charCount = 0;
        boolean underline = false;
        while (curOffset < text.length()) {
            char ch = text.charAt(curOffset);
            if (CharUtils.isDecChar(ch)) {
                out.append(ch);
                ++charCount;
                underline = false;
            } else if (ch == '_') {
                // java7 语法，支持下划线
                if (charCount == 0) {
                    // 不能开头下划线
                    throw new ParseException("INVALID_DEC_NUMBER_START", curOffset);
                }
                underline = true;
            } else {
                // 其他无法识别的字符
                break;
            }
            ++curOffset;
        }
        if (underline) {
            // 不能以下划线结尾
            throw new ParseException("INVALID_DEC_NUMBER_END", curOffset - 1);
        }
        return curOffset - position;
    }
}
