package com.mosect.lib.codeparser;

import com.mosect.lib.codeparser.util.TextUtils;

public class ErrorInfo {

    public static ErrorInfo make(String msg, CharSequence text, int start, int end, int position) {
        if (null == msg || msg.length() == 0) return null;
        ErrorInfo obj = new ErrorInfo();
        int lineIndex = 0;
        int lineOffset = 0;
        int offset = start;
        int posEnd = Math.min(end, position + 1);
        while (offset < posEnd) {
            if (TextUtils.match(text, offset, end, "\r\n")) {
                ++lineIndex;
                lineOffset = 0;
                offset += 2;
            } else if (text.charAt(offset) == '\r' || text.charAt(offset) == '\n') {
                ++lineIndex;
                lineOffset = 0;
                ++offset;
            } else {
                ++lineOffset;
                ++offset;
            }
        }
        obj.setMsg(msg);
        obj.setLineIndex(lineIndex);
        obj.setLineOffset(lineOffset);
        return obj;
    }

    private String msg;
    private int lineIndex;
    private int lineOffset;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public int getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset(int lineOffset) {
        this.lineOffset = lineOffset;
    }

    @Override
    public String toString() {
        return "Error{" +
                "msg='" + msg + '\'' +
                ", lineIndex=" + lineIndex +
                ", lineOffset=" + lineOffset +
                '}';
    }
}
