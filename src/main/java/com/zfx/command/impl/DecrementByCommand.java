package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

public class DecrementByCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        try {
            DatabaseValue value = new DatabaseValue(DataType.STRING,"-1");
            value = db.merge(request.getParam(0),value,(oldValue,newValue)-> {
                if (oldValue!=null) {
                    int decrement = Integer.parseInt(request.getParam(1));
                    oldValue.decrementAndGet(decrement);
                    return oldValue;
                }
                return newValue;
            });
            response.addInt(value.getValue());
        } catch (NumberFormatException e) {
            response.addError("ERR value is not an integer or out of range");
        }
    }
}
