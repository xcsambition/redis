package com.zfx.command.impl;

import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DataType;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import java.util.HashMap;
import java.util.Map;

public class HashSetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        String key = request.getParam(0);
        String hKey = request.getParam(1);
        String hValue = request.getParam(2);

        if (db.isType(key, DataType.HASH)) {
            DatabaseValue value = new DatabaseValue(DataType.HASH);
            HashMap<String, String> map = new HashMap<>();
            map.put(hKey, hValue);
            value.setValue(map);
            db.merge(key, value, (oldValue, newValue) -> {
                if (oldValue == null) {
                    return newValue;
                } else {
                    Map<String, String> oldMap = oldValue.getValue();
                    Map<String, String> newMap = newValue.getValue();
                    oldMap.putAll(newMap);
                    return oldValue;
                }
            });
        }
    }
}
