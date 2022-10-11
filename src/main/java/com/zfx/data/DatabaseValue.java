package com.zfx.data;

public class DatabaseValue {

    private DataType type;

    private Object value;

    public DatabaseValue(DataType type) {
        this.type = type;
    }

    public DatabaseValue(DataType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public <T> T getValue() {
        return (T) value;
    }

    public <T> void setValue(T value) {
        this.value = value;
    }

    /**
     * @return
     * @throws NumberFormatException
     */
    public int incrementAndGet(int increment) throws NumberFormatException {
        int i = Integer.parseInt(value.toString() + increment);
        this.value = String.valueOf(i);
        return i;
    }

    /**
     * @return
     * @throws NumberFormatException
     */
    public int decrementAndGet(int decrement) throws NumberFormatException {
        int i = Integer.parseInt(value.toString() + decrement);
        this.value = String.valueOf(i);
        return i;
    }
}
