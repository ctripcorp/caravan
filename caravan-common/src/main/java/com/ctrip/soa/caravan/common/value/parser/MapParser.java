package com.ctrip.soa.caravan.common.value.parser;

import java.util.HashMap;
import java.util.Map;

import com.ctrip.soa.caravan.common.collect.KeyValuePair;
import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MapParser implements ValueParser<Map<String, String>> {

    public static final MapParser DEFAULT = new MapParser();

    @Override
    public Map<String, String> parse(String value) {
        if (StringValues.isNullOrWhitespace(value))
            return null;

        Map<String, String> map = new HashMap<String, String>();
        String[] pairValues = value.trim().split(",");
        for (String pairValue : pairValues) {
            KeyValuePair<String, String> pair = StringValues.toKeyValuePair(pairValue);
            if (pair == null)
                continue;

            map.put(pair.getKey(), pair.getValue());
        }

        return map.size() == 0 ? null : map;
    }

}
