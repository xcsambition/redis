package com.zfx.command;


import com.github.tonivade.resp.annotation.ParamLength;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.command.RespCommand;
import com.github.tonivade.resp.command.ServerContext;
import com.github.tonivade.resp.command.Session;
import com.github.tonivade.resp.protocol.RedisToken;
import com.zfx.DBServerState;
import com.zfx.DBSessionState;
import com.zfx.data.DataType;
import com.zfx.data.Database;
import com.zfx.data.DatabaseKey;

public class DBCommandWrapper implements RespCommand {

    private int params;

    private DataType dataType;

    private final Object command;

    public DBCommandWrapper(Object command) {
        this.command = command;
        ParamLength length = command.getClass().getAnnotation(ParamLength.class);
        if (length != null) {
            this.params = length.value();
        }

        ParamType type = command.getClass().getAnnotation(ParamType.class);
        if (type != null) {
            this.dataType = type.value();
        }

    }

    @Override
    public RedisToken execute(Request request) {
        Database db = (Database) getCurrentDB(request);
        if (request.getLength() < params) {
            return null;
        } else if (dataType != null && !db.isType(DatabaseKey.safeKey(request.getParam(0)), dataType)) {
            return RedisToken.error("WRONGTYPE Operation against a key holding the wrong kind of value");
        }
        if (command instanceof DBCommand) {
            return executeDBCommand(db, request);
        } else if (command instanceof RespCommand) {
            return executeCommand(request);
        }
        return null;
    }

    private RedisToken executeCommand(Request request) {
        return ((RespCommand) command).execute(request);
    }

    private RedisToken executeDBCommand(Database db, Request request) {
        return ((DBCommand) command).execute(db, request);
    }

    private Database getCurrentDB(Request request) {
        Session session = request.getSession();
        DBSessionState dbsessionState = (DBSessionState) session.getValue("state").get();
        ServerContext serverContext = request.getServerContext();
        DBServerState dbServerState = (DBServerState) serverContext.getValue("state").get();
        Database database = dbServerState.getDatabase(dbsessionState.getCurrentDB());
        return database;
    }
}
