package com.mosect.lib.codeparser;

import java.io.IOException;
import java.util.List;

public interface Node {

    ErrorInfo getError();

    void setError(ErrorInfo error);

    String getType();

    boolean isToken();

    int getChildCount();

    List<Node> getChildren();

    String toText();

    void write(Appendable appendable) throws IOException;

    Node copy();
}
