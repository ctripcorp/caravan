package com.ctrip.soa.caravan.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ctrip.soa.caravan.common.value.parser.BooleanParser;
import com.ctrip.soa.caravan.common.value.parser.IntegerParser;
import com.ctrip.soa.caravan.common.value.parser.ListParser;
import com.ctrip.soa.caravan.common.value.parser.LongParser;
import com.ctrip.soa.caravan.common.value.parser.MapParser;
import com.ctrip.soa.caravan.configuration.typed.DefaultTypedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MyConfigurationManager implements ConfigurationManager {

    @Override
    public String getPropertyValue(String key) {
        Property p = getProperty(key);
        return p.value();
    }

    @Override
    public Property getProperty(String key) {
        MyProperty mp;
        switch (key) {
        case "bool":
            mp = new MyProperty("bKey", "true");
            return new DefaultTypedProperty<Boolean>(mp, BooleanParser.DEFAULT);
        case "int":
            mp = new MyProperty("IKey", "100");
            return new DefaultTypedProperty<Integer>(mp, IntegerParser.DEFAULT);
        case "long":
            mp = new MyProperty("LKey", "9999999");
            return new DefaultTypedProperty<Long>(mp, LongParser.DEFAULT);
        case "list":
            mp = new MyProperty("IKey", "10.103,10.104");
            return new DefaultTypedProperty<List<String>>(mp, ListParser.DEFAULT);
        case "map":
            mp = new MyProperty("IKey", "b:123,c:456");
            return new DefaultTypedProperty<Map<String, String>>(mp, MapParser.DEFAULT);
        default:
            break;
        }
        return new MyProperty("myKey", "myValue");
    }

    public Property getProperty(String key, String pKey, String pValue) {
        MyProperty mp;
        switch (key) {
        case "bool":
            mp = new MyProperty(pKey, pValue);
            return new DefaultTypedProperty<Boolean>(mp, BooleanParser.DEFAULT);
        case "int":
            mp = new MyProperty(pKey, pValue);
            return new DefaultTypedProperty<Integer>(mp, IntegerParser.DEFAULT);
        case "long":
            mp = new MyProperty(pKey, pValue);
            return new DefaultTypedProperty<Long>(mp, LongParser.DEFAULT);
        case "list":
            mp = new MyProperty(pKey, pValue);
            return new DefaultTypedProperty<List<String>>(mp, ListParser.DEFAULT);
        case "map":
            mp = new MyProperty(pKey, pValue);
            return new DefaultTypedProperty<Map<String, String>>(mp, MapParser.DEFAULT);
        default:
            break;
        }
        return new MyProperty(pKey, pValue);
    }

    @Override
    public Collection<ConfigurationSource> sources() {
        return null;
    }

}