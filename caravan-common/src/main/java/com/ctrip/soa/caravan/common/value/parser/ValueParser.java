package com.ctrip.soa.caravan.common.value.parser;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ValueParser<T> {

    T parse(String value);

}
