package com.zfx.annoation;

import com.zfx.data.DataType;

import java.lang.annotation.*;

/**
 * @author zfx
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ParamType {
    DataType value();
}
