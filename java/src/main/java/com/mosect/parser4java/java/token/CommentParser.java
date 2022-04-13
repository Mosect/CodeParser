package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.Token;
import com.mosect.parser4java.java.Constants;

public class CommentParser extends com.mosect.parser4java.javalike.CommentParser {

    @Override
    public String getName() {
        return Constants.PARSER_COMMENT;
    }

    @Override
    public Token makeToken() {
        String text = getParseText().toString();
        return new CommentToken(Constants.TOKEN_COMMENT, text, getCommentName());
    }
}
