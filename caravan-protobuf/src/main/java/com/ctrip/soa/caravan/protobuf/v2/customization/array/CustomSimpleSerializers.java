package com.ctrip.soa.caravan.protobuf.v2.customization.array;

import com.ctrip.soa.caravan.protobuf.v2.customization.TypeCustomizationFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.type.ArrayType;
import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Created by marsqing on 20/04/2017.
 */
@SuppressWarnings("serial")
public class CustomSimpleSerializers extends SimpleSerializers {

  private ArrayProvider provider;

  public CustomSimpleSerializers(ArrayProvider provider) {
    this.provider = provider;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public JsonSerializer<?> findArraySerializer(SerializationConfig config, ArrayType type, BeanDescription beanDesc, TypeSerializer elementTypeSerializer,
      JsonSerializer<Object> elementValueSerializer) {

    Class<?> contentClass = type.getContentType().getRawClass();
    TypeCustomizationFactory factory = provider.factoryForArrayOf(contentClass);

    if (factory != null) {
      final JsonSerializer ser = factory.createSerializer();
      return new JsonSerializer() {

        @SuppressWarnings("unchecked")
        @Override
        public void serialize(Object o, JsonGenerator gen, SerializerProvider serializerProvider)
            throws IOException {

          gen.writeStartArray();
          gen.setCurrentValue(o);

          int length = Array.getLength(o);
          for (int i = 0; i < length; i++) {
            Object arrayElement = Array.get(o, i);
            ser.serialize(arrayElement, gen, serializerProvider);
          }

          gen.writeEndArray();
        }
      };
    }

    return super.findArraySerializer(config, type, beanDesc, elementTypeSerializer, elementValueSerializer);
  }

}
