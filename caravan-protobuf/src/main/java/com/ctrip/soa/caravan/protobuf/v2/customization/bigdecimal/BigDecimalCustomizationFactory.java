package com.ctrip.soa.caravan.protobuf.v2.customization.bigdecimal;

import java.math.BigDecimal;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractTypeCustomizationFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType.ScalarType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.FieldElement;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.FieldElement.Label;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement.Builder;

/**
 * Created by marsqing on 17/03/2017.
 */
public class BigDecimalCustomizationFactory extends AbstractTypeCustomizationFactory<BigDecimal> {

  private final static String TYPE_NAME = "CaravanDotNetDecimalProtobuf";

  public BigDecimalCustomizationFactory(ObjectMapper mapper) {
    super(mapper);
  }

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        BigDecimal.class
    };
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return ScalarType.NamedType.create(TYPE_NAME);
  }

  @Override
  public Builder getMessageElementBuilder() {
    FieldElement.Builder lowFieldBuilder = FieldElement.builder() //
        .name("low") //
        .type(ScalarType.UINT64) //
        .label(Label.OPTIONAL) //
        .tag(1);

    FieldElement.Builder highFieldBuilder = FieldElement.builder() //
        .name("high") //
        .type(ScalarType.UINT32) //
        .label(Label.OPTIONAL) //
        .tag(2);

    FieldElement.Builder scaleFieldBuilder = FieldElement.builder() //
        .name("signScale") //
        .type(ScalarType.UINT32) //
        .label(Label.OPTIONAL) //
        .tag(3);

    return MessageElement.builder() //
        .name(TYPE_NAME) //
        .addField(lowFieldBuilder.build()) //
        .addField(highFieldBuilder.build()) //
        .addField(scaleFieldBuilder.build());
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public JsonSerializer<BigDecimal> createSerializer() {
    JavaType type = typeFactory.constructType(DotNetDecimalProtobuf.class);
    BuilderAndBeanPropertiyWriters bp = constructBeanSerializerBuilder(type);

    return (JsonSerializer) new BigDecimalSerializer(type, bp.builder, bp.properties, null);
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public JsonDeserializer<BigDecimal> createDeserializer() {
    JavaType type = typeFactory.constructType(DotNetDecimalProtobuf.class);
    BuilderAndPropertyMap bp = constructBeanDeserializerBuilder(type);

    return (JsonDeserializer) new BigDecimalDeserializer(bp.builder, bp.beanDesc, bp.propertyMap, null, null, true, false);
  }

}
