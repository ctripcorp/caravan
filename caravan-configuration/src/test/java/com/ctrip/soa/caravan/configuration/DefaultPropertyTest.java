package com.ctrip.soa.caravan.configuration;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ctrip.soa.caravan.configuration.typed.DefaultTypedProperty;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
@SuppressWarnings("unchecked")
public class DefaultPropertyTest {

    @Test
    public void test() {
        MyConfigurationManager manager = new MyConfigurationManager();
        MyProperty mp = (MyProperty) manager.getProperty("");
        System.out.println(mp.value());
        TypedProperty<Boolean> dbp = (DefaultTypedProperty<Boolean>) manager.getProperty("bool");
        System.out.println(dbp.typedValue().booleanValue());
        TypedProperty<Integer> dip = (DefaultTypedProperty<Integer>) manager.getProperty("int");
        System.out.println(dip.typedValue().intValue());
        TypedProperty<Long> dlp = (DefaultTypedProperty<Long>) manager.getProperty("long");
        System.out.println(dlp.typedValue().longValue());
        TypedProperty<List<String>> dlsp = (DefaultTypedProperty<List<String>>) manager.getProperty("list");
        System.out.println(dlsp.typedValue());
        TypedProperty<Map<String, String>> dmp = (DefaultTypedProperty<Map<String, String>>) manager.getProperty("map");
        System.out.println(dmp.typedValue());
    }
}
