package com.zfx.command.impl;

import com.zfx.annoation.ParamLength;
import com.zfx.annoation.ParamType;
import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import static com.zfx.data.DatabaseValue.string;

@ParamLength(2)
@ParamType(DataType.HASH)
public class IncrementByCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        try {
            DatabaseValue value = db.merge(request.getParam(0), string("1"), (oldValue, newValue) -> {
                if (oldValue != null) {
                    int increment = Integer.parseInt(request.getParam(1));
                    oldValue.incrementAndGet(increment);
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
