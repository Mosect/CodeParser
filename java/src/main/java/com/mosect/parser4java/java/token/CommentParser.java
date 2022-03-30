package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;

public class CommentParser extends com.mosect.parser4java.javalike.CommentParser {

    @Override
    public String getName() {
        return "java.comment";
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new CommentToken(getName(), text, getCommentName());
    }
}
