package com.ctrip.soa.caravan.protobuf.v2.customization.bigdecimal;

import java.util.HashSet;
import java.util.Map;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractBeanDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;

/**
 * Created by marsqing on 16/03/2017.
 */
public class BigDecimalDeserializer extends AbstractBeanDeserializer {

  private static final long serialVersionUID = 1L;

  public BigDecimalDeserializer(BeanDeserializerBuilder builder,
      BeanDescription beanDesc, BeanPropertyMap properties,
      Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps,
      boolean ignoreAllUnknown, boolean hasViews) {
    super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
  }

  @Override
  protected Object convertDeserializedToActualFieldValue(Object deserializedBean) {
    DotNetDecimalProtobuf decimal = (DotNetDecimalProtobuf) deserializedBean;

    byte[] bytes = new byte[16];

    long low = decimal.getLow();
    int high = decimal.getHigh();
    int signScale = decimal.getSignScale();

    bytes[0] = (byte) ((signScale & 0xFF000000) >> 24);
    bytes[1] = (byte) ((signScale & 0x00FF0000) >> 16);
    bytes[2] = (byte) ((signScale & 0x0000FF00) >> 8);
    bytes[3] = (byte) (signScale & 0x000000FF);

    bytes[4] = (byte) ((high & 0xFF000000) >> 24);
    bytes[5] = (byte) ((high & 0x00FF0000) >> 16);
    bytes[6] = (byte) ((high & 0x0000FF00) >> 8);
    bytes[7] = (byte) (high & 0x000000FF);

    bytes[8] = (byte) ((low & 0xFF00000000000000L) >> 56);
    bytes[9] = (byte) ((low & 0x00FF000000000000L) >> 48);
    bytes[10] = (byte) ((low & 0x0000FF0000000000L) >> 40);
    bytes[11] = (byte) ((low & 0x000000FF00000000L) >> 32);
    bytes[12] = (byte) ((low & 0x00000000FF000000L) >> 24);
    bytes[13] = (byte) ((low & 0x0000000000FF0000L) >> 16);
    bytes[14] = (byte) ((low & 0x000000000000FF00L) >> 8);
    bytes[15] = (byte) (low & 0x00000000000000FFL);

    return DecimalConverter.netDecimalToBigDecimal(signScale, bytes);
  }

}
