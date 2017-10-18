package com.ctrip.soa.caravan.common.value.parser;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class StringParser implements ValueParser<String> {

    public static final StringParser DEFAULT = new StringParser();

    @Override
    public String parse(String value) {
        return value;
    }

}
