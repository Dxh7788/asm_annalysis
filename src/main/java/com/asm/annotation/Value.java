package com.asm.annotation;


import java.lang.annotation.*;

/**
 * @author dongxiaohong
 * @date 2018/12/20 16:53
 */
@Target({ElementType.TYPE,ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
}
