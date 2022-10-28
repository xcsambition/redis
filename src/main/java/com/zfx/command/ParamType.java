package com.zfx.command;


import com.zfx.data.DataType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ParamType {
    DataType value();
}
