package com.zfx.command.impl;


import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.Database;
import com.zfx.data.IDatabase;

public class ExistsCommand implements ICommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {

            response.addInt(db.containsKey(request.getParam(0)));

    }

}
