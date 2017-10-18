package com.ctrip.soa.caravan.protobuf.v2.customization.map;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractBeanSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by marsqing on 20/04/2017.
 */
@SuppressWarnings("serial")
public class MapSerializer extends AbstractBeanSerializer {

  public MapSerializer(JavaType type, BeanSerializerBuilder builder,
      BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
    super(type, builder, properties, filteredProperties);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  protected Object convertBeanToSerialize(Object bean) {
    Map<?, ?> map = (Map) bean;

    List toSerialize = new ArrayList<>(map.size());

    if (!map.isEmpty()) {
      Class<?> pairClass = _beanType.getRawClass();

      try {
        for (Map.Entry entry : map.entrySet()) {
          Object pair = pairClass.newInstance();
          DynamicClassFactory.INSTANCE.setKeyValue(pair, entry.getKey(), entry.getValue());
          toSerialize.add(pair);
        }
      } catch (Exception e) {
        throw new RuntimeException("Unexpected error", e);
      }
    }

    return toSerialize;
  }

}
