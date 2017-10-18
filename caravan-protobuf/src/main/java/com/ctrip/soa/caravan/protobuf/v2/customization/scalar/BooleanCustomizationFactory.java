package com.ctrip.soa.caravan.protobuf.v2.customization.scalar;

import java.io.IOException;

import com.ctrip.soa.caravan.protobuf.v2.customization.TypeCustomizationFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufGenerator;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufParser;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType.ScalarType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement.Builder;

/**
 * Created by marsqing on 17/03/2017.
 */
@SuppressWarnings("serial")
public class BooleanCustomizationFactory implements TypeCustomizationFactory<Boolean> {

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        boolean.class,
        Boolean.class
    };
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return ScalarType.UINT32;
  }

  @Override
  public JsonSerializer<Boolean> createSerializer() {
    return new StdScalarSerializer<Boolean>(Boolean.class) {

      @Override
      public void serialize(Boolean value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        ProtobufGenerator pgen = (ProtobufGenerator) gen;
        pgen.writeNumber(value ? 1 : 0);
      }
    };
  }

  @Override
  public JsonDeserializer<Boolean> createDeserializer() {
    return new StdScalarDeserializer<Boolean>(Boolean.class) {

      @Override
      public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ProtobufParser pp = (ProtobufParser) p;
        return pp.getIntValue() == 1 ? Boolean.TRUE : Boolean.FALSE;
      }
    };
  }

  @Override
  public Builder getMessageElementBuilder() {
    return null;
  }

}
