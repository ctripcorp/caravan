package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.calendar;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractBeanDeserializer;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.DotNetTimeRelatedProtobuf;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.TimeRelatedConverter;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by marsqing on 21/03/2017.
 */
public class CalendarDeserializer extends AbstractBeanDeserializer {

  private static final long serialVersionUID = 1L;

  public CalendarDeserializer(BeanDeserializerBuilder builder,
      BeanDescription beanDesc, BeanPropertyMap properties,
      Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps,
      boolean ignoreAllUnknown, boolean hasViews) {
    super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
  }

  @Override
  protected Object convertDeserializedToActualFieldValue(Object deserializedBean) {
    return TimeRelatedConverter.netDateTimeToCalendar((DotNetTimeRelatedProtobuf) deserializedBean);
  }
}
