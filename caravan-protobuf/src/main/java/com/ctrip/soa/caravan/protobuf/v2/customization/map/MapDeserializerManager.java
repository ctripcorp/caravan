package com.ctrip.soa.caravan.protobuf.v2.customization.map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.SimpleType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by marsqing on 24/04/2017.
 * Only one instance will be created for all Map.
 *
 * withResolved() will be called by Jackson and will create dedicate
 * MapDeserializer for every content type.
 */
@SuppressWarnings("serial")
public class MapDeserializerManager extends CustomCollectionDeserializer {

  private ConcurrentMap<JavaType, MapDeserializer> deserializers = new ConcurrentHashMap<>();

  private MapCustomizationFactory factory;

  public MapDeserializerManager(MapCustomizationFactory factory) {
    /**
     * The first parameter is just a placeholder, won't be used.
     * So any element type is ok.
     */
    super(
        CollectionType.construct(ArrayList.class, null, null, null, //
            SimpleType.constructUnsafe(Object.class)), //
        null, null, new ValueInstantiator() {
          @SuppressWarnings("rawtypes")
          @Override
          public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new ArrayList();
          }
        });

    this.factory = factory;
  }

  @Override
  public Object deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
    throw new RuntimeException("Should not be called, probably a bug");
  }

  protected CustomCollectionDeserializer withResolved(JavaType pairType, JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd,
      NullValueProvider nuller,
      Boolean unwrapSingle) {

    MapDeserializer deserializer = deserializers.get(pairType);
    if (deserializer == null) {
      deserializer = factory.createDeserializer(pairType);
      deserializer = deserializer.withResolved(pairType, dd, vd, vtd, nuller, unwrapSingle);

      deserializers.put(pairType, deserializer);
    }

    return deserializer;
  }
}
