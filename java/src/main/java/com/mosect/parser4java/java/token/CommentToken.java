package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.common.CommonToken;

public class CommentToken extends CommonToken {

    private final String commentType;

    protected CommentToken(String type, String text, String commentType) {
        super(type, text);
        this.commentType = commentType;
    }

    public String getCommentType() {
        return commentType;
    }
}
