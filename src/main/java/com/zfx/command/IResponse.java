package com.zfx.command;

import com.zfx.data.DatabaseValue;

import java.util.Collection;

public interface IResponse {

    IResponse addValue(DatabaseValue value);

    IResponse addArrayValue(Collection<DatabaseValue> array);

    IResponse addArray(Collection<String> array);


    IResponse addBulkStr(String str);

    IResponse addSimpleStr(String str);

    IResponse addInt(String str);

    IResponse addInt(int value);

    IResponse addInt(boolean value);


    IResponse addError(String str);

}
