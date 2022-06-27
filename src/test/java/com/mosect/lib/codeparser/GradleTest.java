package com.mosect.lib.codeparser;

import static org.junit.Assert.assertEquals;

import com.mosect.lib.codeparser.gradle.DynamicStringParser;
import com.mosect.lib.codeparser.gradle.GradleParser;
import com.mosect.lib.codeparser.java.StringToken;

import org.junit.Test;

public class GradleTest {
    private final GradleParser gradleParser = new GradleParser();

    @Test
    public void testDynamicString() {
        String text = "\"abc ${ v ? \"add\" : 'ok' } def ${ aaa \"${ 3 + \"num\" }\" } gh\"";
        DynamicStringParser parser = new DynamicStringParser();
        NodeInfo out = new NodeInfo();
        parser.parse(text, 0, 0, text.length(), out);
        String value = ((StringToken) out.node).getValue();
        assertEquals("abc ${ v ? \"add\" : 'ok' } def ${ aaa \"${ 3 + \"num\" }\" } gh", value);
    }
}
