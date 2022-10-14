package com.zfx.command.impl;

import com.zfx.annoation.ParamLength;
import com.zfx.annoation.ParamType;
import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import java.util.ArrayList;
import java.util.Set;

@ParamLength(1)
public class KeysAllCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        if ("*".equals(request.getParam(0))) {
            Set<String> sets = db.keySet();
            ArrayList<DatabaseValue> array = new ArrayList<>(sets.size());
            for (String set : sets) {
                array.add(new DatabaseValue(DataType.STRING,set));
            }
            response.addArrayValue(array);
        }else {
            response.addError("not support the command");
        }
    }
}
