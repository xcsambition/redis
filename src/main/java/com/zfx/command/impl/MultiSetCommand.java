package com.zfx.command.impl;

import com.zfx.annoation.ParamLength;
import com.zfx.command.ICommand;
import com.zfx.command.IRequest;
import com.zfx.command.IResponse;
import com.zfx.data.DatabaseValue;
import com.zfx.data.IDatabase;

import static com.zfx.data.DatabaseValue.string;

@ParamLength(2)
public class MultiSetCommand implements ICommand {
    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        String key = null;
        for (String value : request.getParams()) {
            if (key==null) {
                key = value;
            }else {
                db.merge(key, string(value),(oldValue, newValue)->newValue);
                key =null;
            }
        }
        response.addSimpleStr(OK);

    }
}
