package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

public class IncrementCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        try {
            DatabaseValue value = new DatabaseValue();
            value.setType(DataType.INTEGER);
            value.setValue("0");
            db.putIfAbsent(request.getParam(0),value);
            int i = db.get(request.getParam(0)).incrementAndGet();
            response.addInt(i);
        }catch (NumberFormatException e) {
            response.addError("ERR value is not a integer or out of range");
        }



    }
}
