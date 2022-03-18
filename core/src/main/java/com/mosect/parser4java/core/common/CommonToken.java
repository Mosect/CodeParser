package com.mosect.parser4java.core.common;

import com.mosect.parser4java.core.Node;
import com.mosect.parser4java.core.Token;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 通用token
 */
public class CommonToken implements Token {

    private String id;
    private String name;
    private String type;
    private String text;

    public CommonToken(String id, String name, String type, String text) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.text = text;
    }

    public CommonToken() {
    }

    @Override
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isToken() {
        return true;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String getText() {
        return text;
    }

    protected void setText(String text) {
        this.text = text;
    }

    @Override
    public Iterator<Node> iterator() {
        return new Iterator<Node>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Node next() {
                return null;
            }
        };
    }
}
