package com.zfx.command.impl;


import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.Database;
import com.zfx.data.IDatabase;

public class EchoCommand implements ICommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {

            response.addBulkStr(request.getParam(0));

    }

}
