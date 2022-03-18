package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.util.CharUtils;

import java.util.List;

/**
 * 字符节点工厂
 */
public class CharNodeFactory implements NodeFactory {

    private final CharInfo out = new CharInfo();

    @Override
    public String getName() {
        return "char";
    }

    @Override
    public Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        CharSequence text = source.getText();
        int pos = source.getPosition();
        char first = text.charAt(pos);
        if (first == '\'') {
            int nextPos = pos + 1;
            if (nextPos < text.length()) {
                char next = text.charAt(nextPos);
                switch (next) {
                    case '\r':
                    case '\n':
                    case '\'':
                        // 丢失字符内容
                        throw new ParseException("MISSING_CHAR_CONTENT", nextPos);

                    default:
                        boolean ok = parseOneChar(text, pos + 1, out);
                        if (ok) {
                            int end = -1;
                            if (out.end < text.length()) {
                                char endChar = text.charAt(out.end);
                                if (endChar == '\'') {
                                    end = out.end + 1;
                                }
                            }
                            if (end < 0) {
                                throw new ParseException("MISSING_CHAR_END", out.end);
                            }
                            String str = text.subSequence(pos, end).toString();
                            return createCharToken("", str, out.type, out.value);
                        } else {
                            throw new ParseException("INVALID_CHAR", out.end);
                        }
                }
            } else {
                // 无足够字符，丢失字符内容
                throw new ParseException("MISSING_CHAR_CONTENT", pos);
            }
        }
        return null;
    }

    /**
     * 创建字符token
     *
     * @param name     字符token名称
     * @param text     字符文本
     * @param charType 字符类型
     * @param value    字符值
     * @return 字符token
     */
    protected CharToken createCharToken(String name, String text, CharType charType, char value) {
        return new CharToken(name, text, charType, value);
    }

    /**
     * 解析单个字符
     *
     * @param text   文本
     * @param offset 偏移量
     * @param out    输出
     * @return true，解析成功；false，解析失败
     */
    public boolean parseOneChar(CharSequence text, int offset, CharInfo out) {
        int mode = 0;
        int size = 0;
        int state = 0; // 状态：0，正常；1，停止循环；2，停止循环并且有错误
        for (int i = offset; i < text.length() && state == 0; i++) {
            char ch = text.charAt(i);
            switch (mode) {
                case 0:
                    if (ch == '\\') {
                        // 转义
                        mode = 1;
                    } else {
                        // 非转义
                        out.start = offset;
                        out.end = out.start + 1;
                        out.text = text;
                        out.value = ch;
                        out.type = CharType.NORMAL;
                        return true;
                    }
                    break;
                case 1: // 开始转义
                    char ech = getEscapedChar(ch);
                    if (ech == 0) {
                        // 无法转义字符
                        switch (ch) {
                            case 'u': // \u
                                mode = 2;
                                break;
                            case '0': // \0
                                mode = 3;
                                break;
                            default: // 非法转义
                                if (CharUtils.isOctChar(ch)) {
                                    // 8进制
                                    mode = 4;
                                } else {
                                    out.end = i;
                                    state = 2; // 错误
                                }
                                break;
                        }
                    } else {
                        // 可以转义
                        out.type = CharType.ESCAPED;
                        out.start = offset;
                        out.end = i + 1;
                        out.value = ech;
                        out.text = text;
                        return true;
                    }
                    break;
                case 2: // \u
                    if (CharUtils.isHexChar(ch)) {
                        // 合法16进制
                        if (++size == 4) {
                            // 转义结束
                            out.start = offset;
                            out.end = i + 1;
                            out.type = CharType.HEX;
                            out.text = text;
                            String str = text.subSequence(offset + 1, offset + 5).toString();
                            out.value = (char) Integer.parseInt(str, 16);
                            return true;
                        }
                    } else {
                        // 非法16进制
                        state = 2; // 错误
                        out.end = i;
                    }
                    break;
                case 3: // \0
                    if (CharUtils.isOctChar(ch)) {
                        // 8进制
                        size = 2;
                        mode = 4;
                    } else {
                        // 非8进制
                        state = 1; // 停止循环
                    }
                    break;
                case 4: // \xxx
                    if (CharUtils.isOctChar(ch)) {
                        // 8进制
                        if (++size == 3) {
                            // 8进制完成
                            state = 1; // 停止循环
                        }
                    } else {
                        // 非8进制
                        state = 1; // 停止循环
                    }
                    break;
            }
        }

        if (state == 0 || state == 1) {
            switch (mode) {
                case 0: // 未曾开始过
                    out.end = offset;
                    break;
                case 3: // \0
                    out.start = offset;
                    out.end = offset + 2;
                    out.value = '\0';
                    out.text = text;
                    out.type = CharType.ZERO;
                    return true;
                case 4: // \xxx
                    out.start = offset;
                    out.end = offset + size + 1;
                    String str = text.subSequence(offset + 1, offset + 1 + size).toString();
                    out.value = (char) Integer.parseInt(str, 8);
                    out.type = CharType.OCT;
                    out.text = text;
                    return true;
            }
        }

        out.start = offset;
        out.text = text;
        out.value = 0;
        out.type = null;
        return false;
    }

    /**
     * 获取转义后的字符
     * \0例外，不在此方法判断范围内
     *
     * @param ch 字符，可以接在 \ 之后的字符
     * @return 返回转义后的字符，为0表示非合法转义字符
     */
    public char getEscapedChar(char ch) {
        switch (ch) {
            case '\\': // \\
                return '\\';
            case '\'': // \'
                return '\'';
            case '\"': // \"
                return '"';
            case 'b': // \b
                return '\b';
            case 'f': // \f
                return '\f';
            case 'r': // \r
                return '\r';
            case 'n': // \n
                return '\n';
            case 't': // \t
                return '\t';
            default:
                return 0;
        }
    }

}
