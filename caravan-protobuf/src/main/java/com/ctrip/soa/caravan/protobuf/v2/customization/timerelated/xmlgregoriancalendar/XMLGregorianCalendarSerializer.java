package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.xmlgregoriancalendar;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractBeanSerializer;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.TimeRelatedConverter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by marsqing on 21/03/2017.
 */
public class XMLGregorianCalendarSerializer extends AbstractBeanSerializer {

  private static final long serialVersionUID = 1L;

  public XMLGregorianCalendarSerializer(JavaType type, BeanSerializerBuilder builder,
      BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
    super(type, builder, properties, filteredProperties);
  }

  @Override
  protected Object convertBeanToSerialize(Object bean) {
    return TimeRelatedConverter.xmlGregorianCalendarToNetDateTime((XMLGregorianCalendar) bean);
  }

}
