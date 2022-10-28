package com.zfx.command.string;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.annotation.ParamLength;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.protocol.RedisToken;
import com.zfx.command.DBCommand;
import com.zfx.command.ParamType;
import com.zfx.data.DataType;
import com.zfx.data.Database;
import com.zfx.data.DatabaseKey;

@ParamType(DataType.STRING)
@Command("get")
@ParamLength(1)
public class GetCommand implements DBCommand {
    @Override
    public RedisToken execute(Database db, Request request) {
        return convert(db.get(DatabaseKey.safeKey(request.getParam(0))));
    }
}
