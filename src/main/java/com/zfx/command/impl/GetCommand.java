package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.Database;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

public class GetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        DatabaseValue databaseValue = db.get(request.getParam(0));
        if (databaseValue == null) {
            response.addError("不存在"+request.getCommand());
        }else {
            response.addValue(databaseValue);
        }
    }
}
