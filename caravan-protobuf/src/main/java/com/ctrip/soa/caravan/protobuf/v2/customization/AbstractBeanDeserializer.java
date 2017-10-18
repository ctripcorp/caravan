package com.ctrip.soa.caravan.protobuf.v2.customization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by marsqing on 20/03/2017.
 */
public abstract class AbstractBeanDeserializer extends BeanDeserializer {

  private static final long serialVersionUID = 1L;

  public AbstractBeanDeserializer(BeanDeserializerBuilder builder,
      BeanDescription beanDesc,
      BeanPropertyMap properties,
      Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps,
      boolean ignoreAllUnknown, boolean hasViews) {
    super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
  }

  @Override
  public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    Object deserialized = super.deserialize(p, ctxt);

    return convertDeserializedToActualFieldValue(deserialized);
  }

  protected abstract Object convertDeserializedToActualFieldValue(Object deserializedBean);
}
