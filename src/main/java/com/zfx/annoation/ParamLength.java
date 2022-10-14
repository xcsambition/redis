package com.zfx.annoation;


import java.lang.annotation.*;

/**
 * @author zfx
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ParamLength {
    int value();
}
