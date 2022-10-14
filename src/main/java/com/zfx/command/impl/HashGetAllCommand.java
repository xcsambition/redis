package com.zfx.command.impl;

import com.zfx.annoation.ParamLength;
import com.zfx.annoation.ParamType;
import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import java.util.LinkedList;
import java.util.Map;

@ParamLength(1)
@ParamType(DataType.HASH)
public class HashGetAllCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        DatabaseValue value = db.get(request.getParam(0));
        if (value != null) {
            response.addValue(value);

        } else {
            response.addArrayValue(null);
        }

    }
}
