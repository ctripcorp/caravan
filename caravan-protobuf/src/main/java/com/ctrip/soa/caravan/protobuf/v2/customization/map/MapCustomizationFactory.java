package com.ctrip.soa.caravan.protobuf.v2.customization.map;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractTypeCustomizationFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement.Builder;
import java.util.Map;

/**
 * Created by marsqing on 17/03/2017.
 */
@SuppressWarnings("rawtypes")
public class MapCustomizationFactory extends AbstractTypeCustomizationFactory<Map> {

  // .NET BclHelpers.WriteDecimal((bigdecimal)value, dest);

  public MapCustomizationFactory(ObjectMapper mapper) {
    super(mapper);
  }

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        Map.class
    };
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return null;
  }

  @Override
  public Builder getMessageElementBuilder() {
    return null;
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public JsonSerializer<Map> createSerializer() {
    return new MapSerializerManager(this);
  }

  public JsonSerializer createSerializer(ClassPair classPair) {
    JavaType type = typeFactory.constructType(DynamicClassFactory.INSTANCE.fetchOrCreatePairClass(classPair));
    BuilderAndBeanPropertiyWriters bp = constructBeanSerializerBuilder(type);

    return new MapSerializer(type, bp.builder, bp.properties, null);
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public JsonDeserializer createDeserializer() {
    return new MapDeserializerManager(this);
  }

  public MapDeserializer createDeserializer(JavaType pairType) {
    return new MapDeserializer(pairType);
  }

}
