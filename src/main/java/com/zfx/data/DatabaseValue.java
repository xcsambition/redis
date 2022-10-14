package com.zfx.data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zfx
 */
public class DatabaseValue {

    private DataType type;

    private Object value;

    public DatabaseValue(DataType type) {
        this(type, null);
    }

    public DatabaseValue(DataType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * @return the type
     */
    public DataType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(DataType type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    /**
     * @param value the value to set
     */
    public <T> void setValue(T value) {
        this.value = value;
    }

    /**
     * @return
     * @throws NumberFormatException
     */
    public int incrementAndGet(int increment) throws NumberFormatException {
        int i = Integer.parseInt(value.toString()) + increment;
        this.value = String.valueOf(i);
        return i;
    }

    /**
     * @return
     * @throws NumberFormatException
     */
    public int decrementAndGet(int decrement) throws NumberFormatException {
        int i = Integer.parseInt(value.toString()) - decrement;
        this.value = String.valueOf(i);
        return i;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DatabaseValue other = (DatabaseValue) obj;
        if (type != other.type)
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }


    public static DatabaseValue string(String value) {
        return new DatabaseValue(DataType.STRING, value);
    }

    public static DatabaseValue list(String... values) {
        return new DatabaseValue(DataType.LIST,
                Stream.of(values).collect(Collectors.toCollection(LinkedList::new)));
    }

    public static DatabaseValue set(String... values) {
        return new DatabaseValue(DataType.SET,
                Stream.of(values).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    public static DatabaseValue zset(String... values) {
        return new DatabaseValue(DataType.ZSET,
                Stream.of(values).collect(Collectors.toCollection(TreeSet::new)));
    }

    @SafeVarargs
    public static DatabaseValue hash(Map.Entry<String, String>... values) {
        return new DatabaseValue(
                DataType.HASH,
                Stream.of(values).collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public static Map.Entry<String, String> entry(String key, String value) {
        return new AbstractMap.SimpleEntry<String, String>(key, value);
    }
}
