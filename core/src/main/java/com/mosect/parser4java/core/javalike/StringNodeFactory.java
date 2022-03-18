package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;

import java.util.List;

/**
 * 字符串节点工厂
 */
public class StringNodeFactory implements NodeFactory {

    protected CharNodeFactory charNodeFactory;
    protected CharInfo tempCharInfo;
    protected StringBuilder tempStringBuilder;

    public StringNodeFactory() {
        charNodeFactory = new CharNodeFactory();
        tempCharInfo = new CharInfo();
        tempStringBuilder = new StringBuilder();
    }

    @Override
    public String getName() {
        return "string";
    }

    @Override
    public Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        int pos = source.getPosition();
        CharSequence text = source.getText();
        char first = text.charAt(pos);
        if (first == '"') {
            int end = -1;
            int offset = pos + 1;
            boolean loop = true;

            // 清空缓存
            if (tempStringBuilder.length() > 0) {
                tempStringBuilder.delete(0, tempStringBuilder.length());
            }

            while (offset < text.length() && loop) {
                char ch = text.charAt(offset);
                switch (ch) {
                    case '\r':
                    case '\n':
                        throw new ParseException("MISSING_STRING_END", offset);
                    case '"':
                        end = offset + 1;
                        loop = false;
                        break;
                    default: // 解析字符
                        boolean ok = charNodeFactory.parseOneChar(text, offset, tempCharInfo);
                        if (ok) {
                            // 解析字符串成功
                            offset = tempCharInfo.end;
                            tempStringBuilder.append(tempCharInfo.value);
                        } else {
                            // 解析字符失败
                            throw new ParseException("INVALID_STRING", tempCharInfo.end);
                        }
                        break;
                }
            }

            if (end < 0) {
                throw new ParseException("MISSING_STRING_END", offset);
            }
            String str = text.subSequence(pos, end).toString();
            return createStringToken("", str, tempStringBuilder.toString());
        }

        return null;
    }

    protected StringToken createStringToken(String name, String text, String string) {
        return new StringToken(name, text, string);
    }
}
