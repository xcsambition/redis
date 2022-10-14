package com.zfx.command;

import com.zfx.data.DatabaseValue;

import java.util.Collection;

public interface IResponse {

    /**
     * 任意数据类型
     * @param value
     * @return
     */
    IResponse addValue(DatabaseValue value);

    /**
     * 数据类型 数组
     * @param array
     * @return
     */
    IResponse addArrayValue(Collection<DatabaseValue> array);

    /**
     * 字符串数组
     * @param array
     * @return
     */
    IResponse addArray(Collection<String> array);


    /**
     * 增加字符串数据类型
     * @param str
     * @return
     */
    IResponse addBulkStr(String str);

    /**
     * PING、OK
     * @param str
     * @return
     */
    IResponse addSimpleStr(String str);

    /**
     * 整数类型
     * @param str
     * @return
     */
    IResponse addInt(String str);

    /**
     * 整数类型
     * @param value
     * @return
     */
    IResponse addInt(int value);

    /**
     * 整数类型
     * @param value
     * @return
     */
    IResponse addInt(boolean value);


    /**
     * 错误类型
     * @param str
     * @return
     */
    IResponse addError(String str);

}
