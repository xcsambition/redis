package com.zfx.command;

import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.protocol.RedisToken;
import com.zfx.data.Database;
import com.zfx.data.DatabaseValue;

public interface DBCommand {

    RedisToken execute(Database db, Request request);

    default RedisToken convert(DatabaseValue value) {

        return DBResponse.convertValue(value);
    }
}
