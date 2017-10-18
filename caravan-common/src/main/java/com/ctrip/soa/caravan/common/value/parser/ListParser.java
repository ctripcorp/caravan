package com.ctrip.soa.caravan.common.value.parser;

import java.util.ArrayList;
import java.util.List;

import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ListParser implements ValueParser<List<String>> {

    public static final ListParser DEFAULT = new ListParser();

    @Override
    public List<String> parse(String value) {
        if (StringValues.isNullOrWhitespace(value))
            return null;

        List<String> list = new ArrayList<String>();
        String[] array = value.trim().split(",");
        for (String str : array) {
            if (StringValues.isNullOrWhitespace(str))
                continue;

            list.add(str.trim());
        }

        return list.size() == 0 ? null : list;
    }

}
