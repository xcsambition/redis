package com.zfx.data;

public class DatabaseValue {

    private DataType type;

    private String value;

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     * @throws NumberFormatException
     */
    public int incrementAndGet() throws NumberFormatException{
        int i = Integer.parseInt(value);
        this.value = String.valueOf(++i);
        return i;
    }

    /**
     *
     * @return
     * @throws NumberFormatException
     */
    public int decrementAndGet() throws NumberFormatException{
        int i = Integer.parseInt(value);
        this.value = String.valueOf(--i);
        return i;
    }
}
