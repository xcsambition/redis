package com.zfx.data;


/**
 *
 */
public interface DatabaseFactory {

    Database create(String name);

    void clear();


}
