package com.zfx.persistence;

import com.github.tonivade.resp.command.Session;
import com.zfx.DBServerContext;

import static com.github.tonivade.resp.protocol.AbstractRedisToken.ArrayRedisToken;

public class DBCommandProcessor {

    private final DBServerContext server;

    private final Session session;

    public DBCommandProcessor(DBServerContext server, Session session) {
        this.server = server;
        this.session = session;
    }

    public void processCommand(ArrayRedisToken token) {
        // TODO
    }


}
