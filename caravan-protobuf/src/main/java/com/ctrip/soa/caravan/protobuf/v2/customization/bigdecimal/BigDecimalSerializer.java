package com.ctrip.soa.caravan.protobuf.v2.customization.bigdecimal;

import java.math.BigDecimal;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractBeanSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;

/**
 * Created by marsqing on 15/03/2017.
 */
public class BigDecimalSerializer extends AbstractBeanSerializer {

  private static final long serialVersionUID = 1L;

  public BigDecimalSerializer(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties,
      BeanPropertyWriter[] filteredProperties) {
    super(type, builder, properties, filteredProperties);
  }

  @Override
  protected Object convertBeanToSerialize(Object bean) {
    BigDecimal decimal = (BigDecimal) bean;
    byte[] bytes = DecimalConverter.bigDecimalToNetDecimal(decimal);

    long low = 0;
    low |= (long) bytes[8] << 56 & 0xFF00000000000000L;
    low |= (long) bytes[9] << 48 & 0x00FF000000000000L;
    low |= (long) bytes[10] << 40 & 0x0000FF0000000000L;
    low |= (long) bytes[11] << 32 & 0x000000FF00000000L;
    low |= (long) bytes[12] << 24 & 0x00000000FF000000L;
    low |= (long) bytes[13] << 16 & 0x0000000000FF0000L;
    low |= (long) bytes[14] << 8 & 0x000000000000FF00L;
    low |= (long) bytes[15] & 0x00000000000000FFL;

    int high = 0;
    high |= (int) bytes[4] << 24 & 0xFF000000;
    high |= (int) bytes[5] << 16 & 0x00FF0000;
    high |= (int) bytes[6] << 8 & 0x0000FF00;
    high |= (int) bytes[7] & 0x000000FF;

    int signScale = 0;
    signScale |= (int) bytes[0] << 24 & 0xFF000000;
    signScale |= (int) bytes[1] << 16 & 0x00FF0000;
    signScale |= (int) bytes[2] << 8 & 0x0000FF00;
    signScale |= (int) bytes[3] & 0x000000FF;

    signScale = signScale >> 15 & 0x01FE | signScale >> 31 & 0x0001;

    return new DotNetDecimalProtobuf(low, high, signScale);
  }

}
