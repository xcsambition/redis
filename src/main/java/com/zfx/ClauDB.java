package com.zfx;


import com.github.tonivade.resp.RespServer;
import com.github.tonivade.resp.RespServerContext;
import com.github.tonivade.resp.SessionListener;
import com.github.tonivade.resp.command.CommandSuite;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.command.RespCommand;
import com.github.tonivade.resp.protocol.RedisToken;
import com.zfx.command.DBCommandSuite;
import com.zfx.data.Database;
import com.zfx.data.DefaultDatabase;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class ClauDB extends RespServerContext implements DBServerContext {
    private Database db = new DefaultDatabase(new HashMap<>());

    @Override
    public Database getDatabase() {
        return db;
    }

    public ClauDB(String host, int port) {
        this(host, port, new DBCommandSuite(), new DBSessionListener());
    }

    public ClauDB(String host, int port, CommandSuite commands) {
        super(host, port, commands);
    }

    public ClauDB(String host, int port, CommandSuite commands, SessionListener sessionListener) {
        super(host, port, commands, sessionListener);
    }

    @Override
    public void start() {
        super.start();
        init();
    }

    private void init() {
        putValue("state", new DBServerState());

    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected RedisToken executeCommand(RespCommand command, Request request) {
        return super.executeCommand(command, request);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String host = DEFAULT_HOST;
        private int port = DEFAULT_PORT;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public RespServer build() {
            return new RespServer(new ClauDB(host, port));
        }

    }


}
