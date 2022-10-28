package com.zfx.data;

import com.github.tonivade.purefun.Tuple;
import com.github.tonivade.purefun.Tuple2;
import com.github.tonivade.purefun.data.ImmutableList;
import com.github.tonivade.purefun.data.ImmutableMap;
import com.github.tonivade.purefun.data.ImmutableSet;
import com.github.tonivade.resp.protocol.SafeString;
import org.caffinitas.ohc.CacheSerializer;
import org.caffinitas.ohc.Eviction;
import org.caffinitas.ohc.OHCache;
import org.caffinitas.ohc.OHCacheBuilder;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.*;


public class OffHeapDatabaseFactory implements DatabaseFactory {

    @Override
    public Database create(String name) {
        return new OffHeapDatabase(createCache());
    }

    private OHCache<DatabaseKey, DatabaseValue> createCache() {
        return builder()
                .eviction(Eviction.NONE)
                .throwOOME(true)
                .keySerializer(new KeySerializer())
                .valueSerializer(new ValueSerializer())
                .build();
    }

    private OHCacheBuilder<DatabaseKey, DatabaseValue> builder() {
        return OHCacheBuilder.newBuilder();
    }

    @Override
    public void clear() {
        // nothing to do
    }

    private static class KeySerializer implements CacheSerializer<DatabaseKey> {

        @Override
        public void serialize(DatabaseKey key, ByteBuffer buf) {
            writeString(buf, key.getValue());
        }

        @Override
        public DatabaseKey deserialize(ByteBuffer buf) {
            return new DatabaseKey(readString(buf));
        }

        @Override
        public int serializedSize(DatabaseKey key) {
            return stringSize(key.getValue());
        }
    }

    private static class ValueSerializer implements CacheSerializer<DatabaseValue> {

        @Override
        public void serialize(DatabaseValue value, ByteBuffer buf) {
            writeType(buf, value.getType());
            switch (value.getType()) {
                case STRING:
                    writeString(buf, value.getString());
                    break;
                case HASH:
                    ImmutableMap<SafeString, SafeString> hash = value.getHash();
                    writeLength(buf, hash.size());
                    for (Tuple2<SafeString, SafeString> entry : hash.entries()) {
                        writeString(buf, entry.get1());
                        writeString(buf, entry.get2());
                    }
                    break;
                case LIST:
                    ImmutableList<SafeString> list = value.getList();
                    writeLength(buf, list.size());
                    for (SafeString safeString : list) {
                        writeString(buf, safeString);
                    }
                    break;
                case SET:
                    ImmutableSet<SafeString> set = value.getSet();
                    writeLength(buf, set.size());
                    for (SafeString safeString : set) {
                        writeString(buf, safeString);
                    }
                    break;
                case ZSET:
                    NavigableSet<Map.Entry<Double, SafeString>> sortedSet = value.getSortedSet();
                    writeLength(buf, sortedSet.size());
                    for (Map.Entry<Double, SafeString> entry : sortedSet) {
                        writeScore(buf, entry.getKey());
                        writeString(buf, entry.getValue());
                    }
                    break;
                case NONE:
                default:
                    throw new IllegalStateException();
            }
            writeExpireAt(buf, value);
        }

        @Override
        public DatabaseValue deserialize(ByteBuffer buf) {

        }

        @Override
        public int serializedSize(DatabaseValue value) {
            switch (value.getType()) {
                case STRING:
                    SafeString string = value.getString();
                    return typeSize() + stringSize(string) + ttlSize(value.getExpiredAt());
                case HASH:
                    ImmutableMap<SafeString, SafeString> hash = value.getHash();
                    int hashSize = typeSize() + lengthSize();
                    for (Tuple2<SafeString, SafeString> entry : hash.entries()) {
                        hashSize += stringSize(entry.get1());
                        hashSize += stringSize(entry.get2());
                    }
                    return hashSize + ttlSize(value.getExpiredAt());
                case LIST:
                    ImmutableList<SafeString> list = value.getList();
                    int listSize = typeSize() + lengthSize();
                    for (SafeString safeString : list) {
                        listSize += stringSize(safeString);
                    }
                    return listSize + ttlSize(value.getExpiredAt());
                case SET:
                    ImmutableSet<SafeString> set = value.getSet();
                    int setSize = typeSize() + lengthSize();
                    for (SafeString safeString : set) {
                        setSize += stringSize(safeString);
                    }
                    return setSize + ttlSize(value.getExpiredAt());
                case ZSET:
                    NavigableSet<Map.Entry<Double, SafeString>> sortedSet = value.getSortedSet();
                    int sortedSetSize = typeSize() + lengthSize();
                    for (Map.Entry<Double, SafeString> entry : sortedSet) {
                        sortedSetSize += scoreSize() + stringSize(entry.getValue());
                    }
                    return sortedSetSize + ttlSize(value.getExpiredAt());
                default:
                    throw new IllegalStateException();
            }
        }

        private DatabaseValue withExpireAt(ByteBuffer buf, DatabaseValue value) {
            if (readHasExpireAt(buf)) {
                return value.expiredAt(readInstant(buf));
            }
            return value;
        }
    }

    private static void writeString(ByteBuffer buf, SafeString safeString) {
        buf.putInt(safeString.length());
        buf.put(safeString.getBytes());
    }

    private static void writeType(ByteBuffer buf, DataType type) {
        buf.put((byte) type.ordinal());
    }

    private static void writeLength(ByteBuffer buf, int length) {
        buf.putInt(length);
    }

    private static void writeScore(ByteBuffer buf, double score) {
        buf.putDouble(score);
    }

    private static void writeExpireAt(ByteBuffer buf, DatabaseValue value) {
        if (value.getExpiredAt() != null) {
            buf.put((byte) 1);
            buf.putLong(value.getExpiredAt().toEpochMilli());
        } else {
            buf.put((byte) 0);
        }
    }

    private static Instant readInstant(ByteBuffer buf) {
        return Instant.ofEpochMilli(buf.getLong());
    }

    private static DataType readType(ByteBuffer buf) {
        return DataType.values()[buf.get()];
    }

    private static int readLength(ByteBuffer buf) {
        return buf.getInt();
    }

    private static double readScore(ByteBuffer buf) {
        return buf.getDouble();
    }

    private static SafeString readString(ByteBuffer buf) {
        int length = readLength(buf);
        byte[] array = new byte[length];
        buf.get(array);
        return new SafeString(array);
    }

    private static boolean readHasExpireAt(ByteBuffer buf) {
        byte hasExpireAt = buf.get();
        return hasExpireAt != 0;
    }

    private static int scoreSize() {
        return Double.BYTES;
    }

    private static int typeSize() {
        return Byte.BYTES;
    }

    private static int stringSize(SafeString string) {
        return lengthSize() + string.length();
    }

    private static int lengthSize() {
        return Integer.BYTES;
    }

    private static int ttlSize(Instant instant) {
        return Byte.BYTES + (instant != null ? Long.BYTES : 0);
    }
}

