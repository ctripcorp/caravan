package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.xmlgregoriancalendar;

import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.AbstractTimeRelatedCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.DotNetTimeRelatedProtobuf;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by marsqing on 17/03/2017.
 */
public class XMLGregorianCalendarCustomizationFactory extends AbstractTimeRelatedCustomizationFactory<XMLGregorianCalendar> {

  // .NET BclHelpers.WriteDateTime((DateTime)value, dest);

  public XMLGregorianCalendarCustomizationFactory(ObjectMapper mapper) {
    super(mapper);
  }

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        XMLGregorianCalendar.class
    };
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public JsonSerializer<XMLGregorianCalendar> createSerializer() {
    JavaType type = typeFactory.constructType(DotNetTimeRelatedProtobuf.class);
    BuilderAndBeanPropertiyWriters bp = constructBeanSerializerBuilder(type);

    return (JsonSerializer) new XMLGregorianCalendarSerializer(type, bp.builder, bp.properties, null);
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public JsonDeserializer<XMLGregorianCalendar> createDeserializer() {
    JavaType type = typeFactory.constructType(DotNetTimeRelatedProtobuf.class);
    BuilderAndPropertyMap bp = constructBeanDeserializerBuilder(type);

    return (JsonDeserializer) new XMLGregorianCalendarDeserializer(bp.builder, bp.beanDesc, bp.propertyMap, null, null, true, false);
  }

}
