package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

public class DecrementCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        try {
            DatabaseValue value = new DatabaseValue(DataType.STRING,"-1");
            value = db.merge(request.getParam(0),value,(oldValue,newValue)-> {
                if (oldValue == null) {
                    oldValue.decrementAndGet(1);
                    return oldValue;
                }
                return newValue;
            });
            response.addInt(value.getValue());
        }catch (NumberFormatException e) {
            response.addError("ERR value is not a integer or out of range");
        }
    }
}
