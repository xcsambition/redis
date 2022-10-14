package com.zfx.command.impl;

import com.zfx.annoation.ParamLength;
import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import static com.zfx.data.DatabaseValue.string;

@ParamLength(2)
public class SetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        db.put(request.getParam(0), string(request.getParam(1)));
        response.addSimpleStr(OK);
    }
}
