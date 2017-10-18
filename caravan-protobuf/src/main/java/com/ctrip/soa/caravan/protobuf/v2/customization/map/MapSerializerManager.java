package com.ctrip.soa.caravan.protobuf.v2.customization.map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by marsqing on 24/04/2017.
 */
@SuppressWarnings("rawtypes")
public class MapSerializerManager extends JsonSerializer {

  private ConcurrentMap<ClassPair, JsonSerializer> serializers = new ConcurrentHashMap<>();

  private MapCustomizationFactory factory;

  public MapSerializerManager(MapCustomizationFactory factory) {
    this.factory = factory;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    Map<?, ?> map = (Map<?, ?>) o;

    if (!map.isEmpty()) {
      ClassPair classPair = inspectKeyValueClass(map);

      if (classPair.keyClass == null) {
        throw new RuntimeException("Can not serialize map whose keys are all null");
      }

      if (classPair.valueClass == null) {
        throw new RuntimeException("Can not serialize map whose values are all null");
      }

      JsonSerializer serializer = serializers.get(classPair);

      if (serializer == null) {
        serializer = factory.createSerializer(classPair);
        serializers.put(classPair, serializer);
      }

      serializer.serialize(o, jsonGenerator, serializerProvider);
    }
  }

  private ClassPair inspectKeyValueClass(Map<?, ?> map) {
    Class<?> keyClass = null;
    Class<?> valueClass = null;

    for (Map.Entry entry : map.entrySet()) {
      if (keyClass != null && valueClass != null) {
        break;
      }

      if (keyClass == null && entry.getKey() != null) {
        keyClass = entry.getKey().getClass();
      }

      if (valueClass == null && entry.getValue() != null) {
        valueClass = entry.getValue().getClass();
      }
    }

    return new ClassPair(keyClass, valueClass);
  }
}
