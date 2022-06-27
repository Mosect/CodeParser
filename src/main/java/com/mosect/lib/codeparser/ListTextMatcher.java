package com.mosect.lib.codeparser;

import com.mosect.lib.codeparser.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ListTextMatcher implements TextMatcher {

    private final List<String> list = new ArrayList<>();

    public List<String> getList() {
        return list;
    }

    public ListTextMatcher register(String str) {
        getList().add(str);
        return this;
    }

    public void sort() {
        getList().sort((o1, o2) -> {
            if (o1.length() > o2.length()) {
                return -1;
            }
            if (o1.length() < o2.length()) {
                return 1;
            }
            return o1.compareTo(o2);
        });
    }

    @Override
    public String match(CharSequence text, int offset, int end) {
        for (String str : getList()) {
            if (TextUtils.match(text, offset, end, str)) {
                return str;
            }
        }
        return null;
    }
}
