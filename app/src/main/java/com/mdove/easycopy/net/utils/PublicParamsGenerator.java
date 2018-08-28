package com.mdove.easycopy.net.utils;

import java.util.Map;

/**
 * @author MDove on 2018/8/28.
 */
public interface PublicParamsGenerator<T, W> {

    Map<T, W> generate();
}
