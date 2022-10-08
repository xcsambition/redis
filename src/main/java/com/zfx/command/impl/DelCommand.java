package com.zfx.command.impl;


import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.IDatabase;

public class DelCommand implements ICommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {

            db.remove(request.getParam(0));
            response.addSimpleStr(OK);

    }

}
