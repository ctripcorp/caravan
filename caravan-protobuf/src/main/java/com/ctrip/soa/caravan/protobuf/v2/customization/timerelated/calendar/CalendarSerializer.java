package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.calendar;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractBeanSerializer;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.TimeRelatedConverter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import java.util.Calendar;

/**
 * Created by marsqing on 21/03/2017.
 */
public class CalendarSerializer extends AbstractBeanSerializer {

  private static final long serialVersionUID = 1L;

  public CalendarSerializer(JavaType type, BeanSerializerBuilder builder,
      BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
    super(type, builder, properties, filteredProperties);
  }

  @Override
  protected Object convertBeanToSerialize(Object bean) {
    return TimeRelatedConverter.calendarToNetDateTime((Calendar) bean);
  }

}
