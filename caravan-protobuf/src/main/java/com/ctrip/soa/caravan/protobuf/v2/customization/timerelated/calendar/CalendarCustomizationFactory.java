package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.calendar;

import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.AbstractTimeRelatedCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.DotNetTimeRelatedProtobuf;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Calendar;

/**
 * Created by marsqing on 17/03/2017.
 */
public class CalendarCustomizationFactory extends AbstractTimeRelatedCustomizationFactory<Calendar> {

  // .NET BclHelpers.WriteDateTime((DateTime)value, dest);

  public CalendarCustomizationFactory(ObjectMapper mapper) {
    super(mapper);
  }

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        Calendar.class
    };
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public JsonSerializer<Calendar> createSerializer() {
    JavaType type = typeFactory.constructType(DotNetTimeRelatedProtobuf.class);
    BuilderAndBeanPropertiyWriters bp = constructBeanSerializerBuilder(type);

    return (JsonSerializer) new CalendarSerializer(type, bp.builder, bp.properties, null);
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public JsonDeserializer<Calendar> createDeserializer() {
    JavaType type = typeFactory.constructType(DotNetTimeRelatedProtobuf.class);
    BuilderAndPropertyMap bp = constructBeanDeserializerBuilder(type);

    return (JsonDeserializer) new CalendarDeserializer(bp.builder, bp.beanDesc, bp.propertyMap, null, null, true, false);
  }

}
