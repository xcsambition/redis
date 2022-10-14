package com.zfx.command.impl;

import com.zfx.annoation.ParamLength;
import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

@ParamLength(2)
public class SetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        String key = request.getParam(0);
        DatabaseValue value = DatabaseValue.string(request.getParam(1));
        db.merge(key, value, (oldValue, newValue) -> newValue);
    }
}
