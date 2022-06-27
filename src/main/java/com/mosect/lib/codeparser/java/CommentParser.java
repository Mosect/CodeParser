package com.mosect.lib.codeparser.java;

import com.mosect.lib.codeparser.ErrorInfo;
import com.mosect.lib.codeparser.NodeInfo;
import com.mosect.lib.codeparser.NodeParser;
import com.mosect.lib.codeparser.Token;
import com.mosect.lib.codeparser.util.TextUtils;

public class CommentParser implements NodeParser {
    @Override
    public boolean parse(CharSequence text, int start, int offset, int end, NodeInfo out) {
        if (TextUtils.match(text, offset, end, "/*")) {
            int curOffset = offset + 2;
            int commentEnd = end;
            boolean ok = false;
            while (curOffset < end) {
                if (TextUtils.match(text, curOffset, end, "*/")) {
                    commentEnd = curOffset + 2;
                    ok = true;
                    break;
                }
                ++curOffset;
            }
            out.node = new Token("comment", text.subSequence(offset, commentEnd).toString());
            if (!ok) {
                out.node.setError(ErrorInfo.make("MISSING_COMMENT_END", text, start, end, curOffset));
            }
            out.offset = commentEnd;
            return true;
        } else if (TextUtils.match(text, offset, end, "//")) {
            int curOffset = offset + 2;
            int commentEnd = end;
            while (curOffset < end) {
                char ch = text.charAt(curOffset);
                if (ch == '\r' || ch == '\n') {
                    commentEnd = curOffset;
                    break;
                }
                ++curOffset;
            }
            out.node = new Token("comment", text.subSequence(offset, commentEnd).toString());
            out.offset = commentEnd;
            return true;
        }
        return false;
    }
}
