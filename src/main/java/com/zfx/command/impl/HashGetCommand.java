package com.zfx.command.impl;

import com.zfx.annoation.ParamLength;
import com.zfx.annoation.ParamType;
import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import java.util.Map;

@ParamLength(2)
@ParamType(DataType.HASH)
public class HashGetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        DatabaseValue value = db.get(request.getParam(0));
        Map<String, String> map = (Map<String, String>) value.getValue();
        response.addBulkStr(map.get(request.getParam(1)));

    }
}
