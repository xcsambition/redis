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

import static com.zfx.data.DatabaseValue.entry;
import static com.zfx.data.DatabaseValue.hash;

@ParamLength(3)
@ParamType(DataType.HASH)
public class HashSetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        String key = request.getParam(0);
        String hKey = request.getParam(1);
        String hValue = request.getParam(2);
        DatabaseValue value = hash(entry(hKey, hValue));

        DatabaseValue resultValue = db.merge(key, value, (oldValue, newValue) -> {
            if (oldValue == null) {
                return newValue;
            } else {
                Map<String, String> oldMap = oldValue.getValue();
                Map<String, String> newMap = newValue.getValue();
                oldMap.putAll(newMap);
                return oldValue;
            }
        });
        Map<String, String> map = resultValue.getValue();
        response.addInt(map.containsKey(hKey));
    }
}
