package com.mosect.parser4java.java.token;

import com.mosect.parser4java.core.common.CommonToken;

/**
 * 关键字token
 */
public class KeywordToken extends CommonToken {

    private final Data data;

    protected KeywordToken(String type, String text, Data data) {
        super(type, text);
        this.data = data;
    }

    public boolean isModifyClass() {
        return data.modifyClass;
    }

    public boolean isModifyField() {
        return data.modifyField;
    }

    public boolean isModifyMethod() {
        return data.modifyMethod;
    }

    public boolean isBaseType() {
        return data.primitive;
    }

    public boolean isInstructed() {
        return data.instructed;
    }

    public boolean isDefinition() {
        return data.definition;
    }

    public static class Data {

        private boolean modifyClass; // 是否可以修饰类
        private boolean modifyField; // 是否可以修饰字段
        private boolean modifyMethod; // 是否可以修饰方法
        private boolean primitive; // 是否为原始类型
        private boolean instructed; // 是否为之类
        private boolean definition; // 是否为声明

        public boolean isModifyClass() {
            return modifyClass;
        }

        public Data setModifyClass(boolean modifyClass) {
            this.modifyClass = modifyClass;
            return this;
        }

        public boolean isModifyField() {
            return modifyField;
        }

        public Data setModifyField(boolean modifyField) {
            this.modifyField = modifyField;
            return this;
        }

        public boolean isModifyMethod() {
            return modifyMethod;
        }

        public Data setModifyMethod(boolean modifyMethod) {
            this.modifyMethod = modifyMethod;
            return this;
        }

        public boolean isPrimitive() {
            return primitive;
        }

        public Data setPrimitive(boolean primitive) {
            this.primitive = primitive;
            return this;
        }

        public boolean isInstructed() {
            return instructed;
        }

        public Data setInstructed(boolean instructed) {
            this.instructed = instructed;
            return this;
        }

        public boolean isDefinition() {
            return definition;
        }

        public Data setDefinition(boolean definition) {
            this.definition = definition;
            return this;
        }
    }
}
