package com.zfx.data;


import com.github.tonivade.purefun.Equal;
import com.github.tonivade.resp.protocol.SafeString;

import java.util.Objects;

/**
 * 数据 键
 */
public class DatabaseKey {

    private SafeString value;

    public SafeString getValue() {
        return value;
    }

    private static final Equal<DatabaseKey> EQUAL = Equal.<DatabaseKey>of().comparing(k -> k.value);

    public DatabaseKey(SafeString value) {
        this.value = value;
    }

    public static DatabaseKey safeKey(SafeString key) {
        return new DatabaseKey(key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        return EQUAL.applyTo(this, obj);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
