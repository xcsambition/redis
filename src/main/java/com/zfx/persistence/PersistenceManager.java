package com.zfx.persistence;


import com.zfx.DBServerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;

/**
 * 持久化
 */
@Slf4j
public class PersistenceManager {

    private static final int MAX_FRAME_SIZE = 1024 * 1024 * 100;

    private OutputStream out;

    private DBServerContext server;

    private DBCommandProcessor processor;


}
