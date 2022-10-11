package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import java.util.LinkedList;
import java.util.Map;

public class HashGetAllCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        DatabaseValue value = db.get(request.getParam(0));

        if (value != null) {
            if (value.getType() == DataType.HASH) {
                LinkedList<String> result = new LinkedList<>();
                Map<String, String> map = value.getValue();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    result.add(entry.getKey());
                    result.add(entry.getValue());
                }
                response.addArray(result);
            } else {
                response.addError("WRONGTYPE Operation against a key holding the wrong kind of value");
            }

        } else {
            response.addArrayValue(null);
        }

    }
}
