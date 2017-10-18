package com.ctrip.soa.caravan.configuration.typed;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ctrip.soa.caravan.common.value.parser.BooleanParser;
import com.ctrip.soa.caravan.common.value.parser.IntegerParser;
import com.ctrip.soa.caravan.common.value.parser.ListParser;
import com.ctrip.soa.caravan.common.value.parser.LongParser;
import com.ctrip.soa.caravan.common.value.parser.MapParser;
import com.ctrip.soa.caravan.configuration.MyConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
@SuppressWarnings("unchecked")
public class DefaultTypedConfigurationManagerTest {

    @Test
    public void testDefault() {
        MyConfigurationManager myManager = new MyConfigurationManager();
        DefaultTypedConfigurationManager manager = new DefaultTypedConfigurationManager(myManager);

        System.out.println(manager.getProperty("").value());
        System.out.println(manager.getProperty("bool", BooleanParser.DEFAULT).typedValue().booleanValue());
        System.out.println(manager.getProperty("int", IntegerParser.DEFAULT).typedValue().intValue());
        System.out.println(manager.getProperty("long", LongParser.DEFAULT).typedValue().intValue());
        System.out.println(manager.getProperty("list", ListParser.DEFAULT).typedValue());
        System.out.println(manager.getProperty("map", MapParser.DEFAULT).typedValue());
    }

    @Test
    public void testBooleanAbnormal() {
        MyConfigurationManager myManager = new MyConfigurationManager();
        DefaultTypedProperty<Boolean> p = (DefaultTypedProperty<Boolean>) myManager.getProperty("bool", "bKey", "True ");
        assertTrue(p.typedValue().booleanValue());
        p = (DefaultTypedProperty<Boolean>) myManager.getProperty("bool", "bKey", " True ");
        assertTrue(p.typedValue().booleanValue());
        p = (DefaultTypedProperty<Boolean>) myManager.getProperty("bool", "bKey", " True");
        assertTrue(p.typedValue().booleanValue());
        p = (DefaultTypedProperty<Boolean>) myManager.getProperty("bool", "bKey", "TURE");
        assertFalse(p.typedValue().booleanValue());
        p = (DefaultTypedProperty<Boolean>) myManager.getProperty("bool", "bKey", "FALSE");
        assertFalse(p.typedValue().booleanValue());
        p = (DefaultTypedProperty<Boolean>) myManager.getProperty("bool", "bKey", "qwdqwd123");
        assertFalse(p.typedValue().booleanValue());
    }

    @Test
    public void testIntAbnormal() {
        MyConfigurationManager myManager = new MyConfigurationManager();
        DefaultTypedProperty<Integer> p = (DefaultTypedProperty<Integer>) myManager.getProperty("int", "iKey", "123a");
        assertNull(null, p.typedValue());
        p = (DefaultTypedProperty<Integer>) myManager.getProperty("int", "iKey", null);
        assertNull(null, p.typedValue());
        p = (DefaultTypedProperty<Integer>) myManager.getProperty("int", "iKey", "    ");
        assertNull(null, p.typedValue());
    }

    @Test
    public void testLongAbnormal() {
        MyConfigurationManager myManager = new MyConfigurationManager();
        DefaultTypedProperty<Long> p = (DefaultTypedProperty<Long>) myManager.getProperty("long", "iKey", "9999999a");
        assertNull(null, p.typedValue());
        p = (DefaultTypedProperty<Long>) myManager.getProperty("long", "iKey", null);
        assertNull(null, p.typedValue());
        p = (DefaultTypedProperty<Long>) myManager.getProperty("long", "iKey", "    ");
        assertNull(null, p.typedValue());
    }

    @Test
    public void testListAbnormal() {
        MyConfigurationManager myManager = new MyConfigurationManager();
        DefaultTypedProperty<List<String>> p = (DefaultTypedProperty<List<String>>) myManager.getProperty("list", "lKey", "1112,,,2345,,12341,,,  ");
        assertTrue(p.typedValue().size() == 3);
        p = (DefaultTypedProperty<List<String>>) myManager.getProperty("list", "lKey", "    ");
        assertNull(p.typedValue());
        p = (DefaultTypedProperty<List<String>>) myManager.getProperty("list", "lKey", null);
        assertNull(p.typedValue());
        p = (DefaultTypedProperty<List<String>>) myManager.getProperty("list", "lKey", ", ,  ,, ");
        assertNull(p.typedValue());
        System.out.println(p.typedValue());
    }

    @Test
    public void testMapAbnormal() {
        MyConfigurationManager myManager = new MyConfigurationManager();
        DefaultTypedProperty<Map<String, String>> p = (DefaultTypedProperty<Map<String, String>>) myManager.getProperty("map", "mKey", "1112,,,2345,,12341,,,  ");
        assertNull(p.typedValue());
        p = (DefaultTypedProperty<Map<String, String>>) myManager.getProperty("map", "mKey", "    ");
        assertNull(p.typedValue());
        p = (DefaultTypedProperty<Map<String, String>>) myManager.getProperty("map", "mKey", null);
        assertNull(p.typedValue());
        p = (DefaultTypedProperty<Map<String, String>>) myManager.getProperty("map", "mKey", ", ,  ,, ");
        assertNull(p.typedValue());
        p = (DefaultTypedProperty<Map<String, String>>) myManager.getProperty("map", "mKey", ":");
        assertNull(p.typedValue());
        p = (DefaultTypedProperty<Map<String, String>>) myManager.getProperty("map", "mKey", " : :  ");
        assertNull(p.typedValue());
        p = (DefaultTypedProperty<Map<String, String>>) myManager.getProperty("map", "mKey", " :: :  ");
        assertNull(p.typedValue());
    }
}
