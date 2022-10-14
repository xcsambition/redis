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

@ParamLength(1)
@ParamType(DataType.HASH)
public class IncrementCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        try {
            DatabaseValue value  = db.merge(request.getParam(0), string("1"), (oldValue, newValue) -> {
                if (oldValue != null) {
                    oldValue.incrementAndGet(1);
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
