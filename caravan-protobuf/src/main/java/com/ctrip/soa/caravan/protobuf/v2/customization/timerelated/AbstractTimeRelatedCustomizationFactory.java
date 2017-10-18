package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated;

import com.ctrip.soa.caravan.protobuf.v2.customization.AbstractTypeCustomizationFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType.ScalarType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.FieldElement;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.FieldElement.Label;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement.Builder;

/**
 * Created by marsqing on 24/03/2017.
 */
public abstract class AbstractTimeRelatedCustomizationFactory<T> extends AbstractTypeCustomizationFactory<T> {

  protected final static String TYPE_NAME = "CaravanDotNetTimeRelatedProtobuf";

  public AbstractTimeRelatedCustomizationFactory(ObjectMapper mapper) {
    super(mapper);
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return DataType.NamedType.create(TYPE_NAME);
  }

  @Override
  public Builder getMessageElementBuilder() {
    FieldElement.Builder valueFieldBuilder = FieldElement.builder() //
        .name("value") //
        .type(ScalarType.SINT64) //
        .label(Label.OPTIONAL) //
        .tag(1);

    FieldElement.Builder scaleFieldBuilder = FieldElement.builder() //
        .name("scale") //
        .type(ScalarType.INT32) //
        .label(Label.OPTIONAL) //
        .tag(2);

    return MessageElement.builder() //
        .name(TYPE_NAME) //
        .addField(valueFieldBuilder.build()) //
        .addField(scaleFieldBuilder.build());
  }

}
