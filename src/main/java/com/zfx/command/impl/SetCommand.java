package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.Database;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

public class SetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
            DatabaseValue value = new DatabaseValue();
            value.setType(DataType.STRING);
            value.setValue(request.getParam(1));
            String key = request.getParam(0);
            db.put(key,value);
            response.addSimpleStr(OK);

    }
}
