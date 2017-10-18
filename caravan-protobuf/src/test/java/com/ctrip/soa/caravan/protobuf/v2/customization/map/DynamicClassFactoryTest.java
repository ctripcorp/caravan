package com.ctrip.soa.caravan.protobuf.v2.customization.map;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.lang.annotation.Annotation;
import java.util.Date;
import org.junit.Test;

/**
 * Created by marsqing on 24/04/2017.
 */
public class DynamicClassFactoryTest {

  @Test
  public void testCreateClass() throws Exception {
    Class<?> c = DynamicClassFactory.INSTANCE.fetchOrCreatePairClass(new ClassPair(String.class, Date.class));
    KVPair pair = (KVPair) c.newInstance();

    assertEquals(1, c.getInterfaces().length);
    assertEquals(KVPair.class, c.getInterfaces()[0]);

    Annotation[] as = c.getAnnotations();
    assertEquals(1, as.length);
    assertEquals(JsonPropertyOrder.class, as[0].annotationType());

    assertEquals(String.class, c.getMethod("getKey").getReturnType());
    assertEquals(Date.class, c.getMethod("getValue").getReturnType());
    assertEquals(String.class, c.getMethod("setKey", String.class).getParameterTypes()[0]);
    assertEquals(Date.class, c.getMethod("setValue", Date.class).getParameterTypes()[0]);

    Date date = new Date();
    pair.key("key");
    pair.value(date);

    assertEquals("key", pair.key());
    assertEquals(date, pair.value());
  }

}