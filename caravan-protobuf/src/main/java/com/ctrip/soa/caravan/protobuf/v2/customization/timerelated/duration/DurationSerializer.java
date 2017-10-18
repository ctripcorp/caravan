package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.duration;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractBeanSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;

/**
 * Created by marsqing on 24/03/2017.
 */
public class DurationSerializer extends AbstractBeanSerializer {

  private static final long serialVersionUID = 1L;

  public DurationSerializer(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties,
      BeanPropertyWriter[] filteredProperties) {
    super(type, builder, properties, filteredProperties);
  }

  @Override
  protected Object convertBeanToSerialize(Object bean) {
    return null;
  }
}
