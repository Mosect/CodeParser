package com.mosect.parser4java.core.javalike;

import com.mosect.parser4java.core.common.CommonTextParser;
import com.mosect.parser4java.core.util.CharUtils;

/**
 * 注释解析器
 */
public class CommentParser extends CommonTextParser {

    private String commentName;

    @Override
    protected void onClear() {
        super.onClear();
        setCommentName(null);
    }

    @Override
    protected void onParse(CharSequence text, int start) {
        if (CharUtils.match(text, start, "/*", false)) {
            // 多行注释
            setCommentName("multi");
            int offset = start + 2;
            boolean end = false; // 是否找到结束符号
            while (offset < text.length()) {
                if (CharUtils.match(text, offset, "*/", false)) {
                    // 多行注释结束
                    offset += 2;
                    end = true;
                    break;
                } else {
                    ++offset;
                }
            }
            if (!end) {
                putError("COMMENT_MISSING_END", "Missing comment end", offset);
            }
            finishParse(true, offset);
        } else if (CharUtils.match(text, start, "//", false)) {
            setCommentName("single");
            // 当行注释
            int offset = start + 2;
            while (offset < text.length()) {
                char ch = text.charAt(offset);
                if (ch == '\r' || ch == '\n') {
                    break;
                }
                ++offset;
            }
            finishParse(true, offset);
        } else {
            finishParse(false, start);
        }
    }

    public String getCommentName() {
        return commentName;
    }

    protected void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    @Override
    public String getName() {
        return "javalike.comment";
    }

    @Override
    public CharSequence getDisplayParseText() {
        if (isSuccess()) {
            return getCommentName();
        }
        return "<unknown>";
    }
}
