package com.mosect.lib.codeparser;

import com.mosect.lib.codeparser.util.TextUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Token implements Node {

    private final String type;
    private final String text;
    private ErrorInfo error;

    public Token(String type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public ErrorInfo getError() {
        return error;
    }

    @Override
    public void setError(ErrorInfo error) {
        this.error = error;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public final boolean isToken() {
        return true;
    }

    @Override
    public final int getChildCount() {
        return 0;
    }

    @Override
    public final List<Node> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String toText() {
        return getText();
    }

    @Override
    public void write(Appendable appendable) throws IOException {
        appendable.append(getText());
    }

    @Override
    public Token copy() {
        Token token = new Token(getType(), getText());
        token.setError(getError());
        return token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + getType() + '\'' +
                ", text='" + TextUtils.showText(getText()) + '\'' +
                '}';
    }
}
