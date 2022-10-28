package com.zfx.data;

import com.github.tonivade.purefun.Equal;
import com.github.tonivade.purefun.data.ImmutableList;
import com.github.tonivade.purefun.data.ImmutableMap;
import com.github.tonivade.purefun.data.ImmutableSet;
import com.github.tonivade.purefun.data.Sequence;
import com.github.tonivade.resp.protocol.SafeString;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * 数据 值
 */
public class DatabaseValue {

    private final DataType type;

    private final Object value;

    private final Instant expiredAt;

    private static final Equal<DatabaseValue> EQUAL =
            Equal.<DatabaseValue>of().comparing(v -> v.type).comparing(v -> v.value);

    private DatabaseValue(DataType type, Object value) {
        this(type, value, null);
    }

    private DatabaseValue(DataType type, Object value, Instant expiredAt) {
        this.type = type;
        this.value = value;
        this.expiredAt = expiredAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public boolean equals(Object obj) {
        return EQUAL.applyTo(this, obj);
    }

    @Override
    public String toString() {
        return "DatabaseValue [type=" + type + ", value=" + value + "]";
    }

    public DataType getType() {
        return type;
    }


    private <T> T getValue() {
        return (T) value;
    }


    public Instant getExpiredAt() {
        return expiredAt;
    }

    private void requireType(DataType type) {
        if (this.type != type) {
            throw new IllegalStateException("invalid type: " + type);
        }

    }

    public static DatabaseValue string(String value) {
        return string(SafeString.safeString(value));
    }

    public static DatabaseValue string(SafeString value) {
        return new DatabaseValue(DataType.STRING, value);
    }


    public static DatabaseValue list(Sequence<SafeString> values) {
        return new DatabaseValue(DataType.LIST,values.asList());
    }
    public static DatabaseValue hash(Collection<SafeString> values) {
        return new DatabaseValue(DataType.LIST,ImmutableList.from(requireNonNull(values).stream()));
    }

    public static DatabaseValue list(SafeString... values) {
        return new DatabaseValue(DataType.LIST, ImmutableList.from(Stream.of(values)));
    }

    public SafeString getString() {
        requireType(DataType.STRING);
        return (SafeString) getValue();
    }

    public ImmutableMap<SafeString, SafeString> getHash() {
        requireType(DataType.HASH);
        return getValue();
    }

    public ImmutableList<SafeString> getList() {
        requireType(DataType.LIST);
        return getValue();
    }

    public ImmutableSet<SafeString> getSet() {
        requireType(DataType.SET);
        return getValue();
    }

    public NavigableSet<Map.Entry<Double, SafeString>> getSortedSet() {
        requireType(DataType.ZSET);
        return getValue();
    }

    public DatabaseValue expiredAt(Instant instant) {
        return new DatabaseValue(this.type,this.value,instant);
    }

    public boolean isExpired(Instant now) {
        if (expiredAt !=null) {
            return now.isAfter(expiredAt);
        }
        return false;
    }
}
