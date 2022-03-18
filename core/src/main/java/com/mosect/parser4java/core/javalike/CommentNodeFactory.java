package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.NodeFactory;
import com.mosect.parser4java.core.ParentParser;
import com.mosect.parser4java.core.ParseError;
import com.mosect.parser4java.core.ParseException;
import com.mosect.parser4java.core.TextSource;
import com.mosect.parser4java.core.common.CommonToken;

import java.util.List;

/**
 * 注释节点解析器
 */
public class CommentNodeFactory implements NodeFactory {

    @Override
    public String getName() {
        return "comment";
    }

    @Override
    public Node parse(ParentParser parentParser, TextSource source, List<ParseError> outErrors) throws ParseException {
        if (source.match("//")) {
            // 单行注释
            CharSequence text = source.getText();
            int end = text.length();
            int pos = source.getPosition();
            for (int i = pos + 2; i < text.length(); i++) {
                char ch = text.charAt(i);
                if (ch == '\r' || ch == '\n') {
                    end = i;
                    break;
                }
            }
            String tokenText = text.subSequence(pos, end).toString();
            source.setPosition(end);
            return new CommonToken("", "single", "comment", tokenText);
        } else if (source.match("/*")) {
            // 多行注释
            int end = -1;
            CharSequence text = source.getText();
            int pos = source.getPosition();
            boolean star = false;
            for (int i = pos + 2; i < text.length(); i++) {
                char ch = text.charAt(pos);
                if (star) {
                    if (ch == '/') {
                        // 多行注释结束
                        end = pos + 1;
                        break;
                    }
                    if (ch != '*') {
                        star = false;
                    }
                } else {
                    if (ch == '*') {
                        star = true;
                    }
                }
            }
            if (end >= 0) {
                String tokenText = text.subSequence(pos, end).toString();
                return new CommonToken("", "multiple", "comment", tokenText);
            }
            throw new ParseException("COMMENT_MISSING_END", text.length());
        }
        return null;
    }
}
