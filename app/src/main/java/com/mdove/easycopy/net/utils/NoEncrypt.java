package com.mdove.easycopy.net.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author MDove on 2018/8/28.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface NoEncrypt {
}
