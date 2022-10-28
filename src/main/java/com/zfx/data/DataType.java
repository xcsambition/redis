package com.zfx.data;

/**
 * 支持的数据类型
 */
public enum DataType {

    STRING("string"),
    LIST("list"),
    SET("set"),
    ZSET("zset"),
    HASH("hash"),
    NONE("none");

    private final String text;

    DataType(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
