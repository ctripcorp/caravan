package com.ctrip.soa.caravan.protobuf.v2.customization.array;

import com.ctrip.soa.caravan.protobuf.v2.customization.TypeCustomizationFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.type.ArrayType;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by marsqing on 20/04/2017.
 */
@SuppressWarnings("serial")
public class CustomSimpleDeserializers extends SimpleDeserializers {

  private ArrayProvider provider;

  public CustomSimpleDeserializers(ArrayProvider provider) {
    this.provider = provider;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public JsonDeserializer<?> findArrayDeserializer(ArrayType type, DeserializationConfig config, BeanDescription beanDesc,
      TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {

    final Class<?> contentClass = type.getContentType().getRawClass();
    TypeCustomizationFactory factory = provider.factoryForArrayOf(contentClass);

    if (factory != null) {
      final JsonDeserializer des = factory.createDeserializer();
      return new JsonDeserializer() {

        @SuppressWarnings("unchecked")
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
          List result = new LinkedList();

          try {
            while (p.nextToken() != JsonToken.END_ARRAY) {
              Object value = des.deserialize(p, ctxt);
              result.add(value);
            }
          } catch (Exception e) {
            throw new RuntimeException("Unexpected deserialize protobuf error", e);
          }

          Object arrayResult = Array.newInstance(contentClass, result.size());

          int i = 0;
          for (Object o : result) {
            Array.set(arrayResult, i, o);
            i++;
          }

          return arrayResult;
        }
      };
    }

    return super.findArrayDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
  }

}
