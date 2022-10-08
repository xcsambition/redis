package com.zfx.command;

import com.zfx.data.DatabaseValue;

import java.util.Collection;

public interface IResponse {

    IResponse addValue(DatabaseValue value);

    IResponse addBulkStr(String str);

    IResponse addSimpleStr(String str);

    IResponse addInt(String str);

    IResponse addInt(int i);

    IResponse addInt(boolean b);

    IResponse addError(String str);

    IResponse addArray(Collection<DatabaseValue> array);
}
