package com.zfx;


/**
 * 配置类
 */
public class DBConfig {

    private static final int DEFAULT_DATABASES = 10;

    private int numDatabases = DEFAULT_DATABASES;

    public void setNumDatabases(int numDatabases) {
        this.numDatabases = numDatabases;
    }

    public int getNumDatabases() {
        return numDatabases;
    }
}
