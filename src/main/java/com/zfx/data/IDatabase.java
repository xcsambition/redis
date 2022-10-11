package com.zfx.data;

import java.util.Map;

public interface IDatabase extends Map<String, DatabaseValue> {

    boolean isType(String key, DataType type);
}
