package com.zfx;

import com.github.tonivade.resp.command.ServerContext;
import com.zfx.data.Database;

public interface DBServerContext extends ServerContext {

    String DEFAULT_HOST = "localhost";

    int DEFAULT_PORT = 7077;

    Database getDatabase();

}
