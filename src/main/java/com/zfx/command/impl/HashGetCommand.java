package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import java.util.Map;

public class HashGetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        DatabaseValue value = db.get(request.getParam(0));

        if (value!=null) {
            if (value.getType() == DataType.HASH) {
                Map<String,String> map = value.getValue();
                response.addBulkStr(map.get(request.getParam(1)));
            }else {
                response.addError("WRONG TYPE Operation against a key holding the wrong kind of value");
            }
        }else {
            response.addBulkStr(null);
        }

    }
}
