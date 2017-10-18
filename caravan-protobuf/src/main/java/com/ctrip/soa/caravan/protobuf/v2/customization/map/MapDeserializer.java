package com.ctrip.soa.caravan.protobuf.v2.customization.map;

import com.ctrip.soa.caravan.common.collect.KeyValuePair;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marsqing on 20/04/2017.
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class MapDeserializer extends CustomCollectionDeserializer {

  public MapDeserializer(JavaType pairType) {
    super(
        CollectionType.construct(ArrayList.class, null, null, null, pairType), //
        null, null, new ValueInstantiator() {
          @Override
          public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new ArrayList();
          }
        });
  }

  @SuppressWarnings("unchecked")
  private MapDeserializer(JavaType collectionType, JsonDeserializer valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator,
      JsonDeserializer delegateDeser, NullValueProvider nuller, Boolean unwrapSingle) {
    super(collectionType, valueDeser, valueTypeDeser, valueInstantiator, delegateDeser, nuller, unwrapSingle);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    Collection list = (Collection) super.deserialize(p, ctxt);

    Map map = new HashMap();
    for (Object ele : list) {
      try {
        KeyValuePair pair = DynamicClassFactory.INSTANCE.getKeyValue(ele);
        map.put(pair.getKey(), pair.getValue());
      } catch (Exception e) {
        throw new RuntimeException("Unexpected error", e);
      }
    }

    return map;
  }

  // Will be called for every unknown pairType before deserialize is called to construct dedicate instance for every pairType.
  protected MapDeserializer withResolved(JavaType pairType, JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd, NullValueProvider nuller,
      Boolean unwrapSingle) {
    CollectionType containerType = CollectionType.construct(ArrayList.class, null, null, null, pairType);
    return new MapDeserializer(containerType, vd, vtd, this._valueInstantiator, dd, nuller, unwrapSingle);
  }

}
