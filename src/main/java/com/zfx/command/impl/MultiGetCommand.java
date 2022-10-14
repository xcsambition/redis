package com.zfx.command.impl;

import com.zfx.annoation.ParamLength;
import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import java.util.ArrayList;

@ParamLength(1)
public class MultiGetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        ArrayList<DatabaseValue> result = new ArrayList<>(request.getLength());

        for (String key : request.getParams()) {
            result.add(db.get(key));
        }
        response.addArrayValue(result);
    }
}
