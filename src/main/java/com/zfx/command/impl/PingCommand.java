package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.Database;
import com.zfx.data.IDatabase;

public class PingCommand implements ICommand {

    public static final String PONG = "PONG";

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        if (request.getLength() > 0) {
            response.addBulkStr(request.getParam(0));
        } else {
            response.addSimpleStr(PONG);
        }
    }
}
