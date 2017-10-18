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
public class ByteCustomizationFactory implements TypeCustomizationFactory<Byte> {

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        byte.class,
        Byte.class
    };
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return ScalarType.UINT32;
  }

  @Override
  public JsonSerializer<Byte> createSerializer() {
    return new StdScalarSerializer<Byte>(Byte.class) {

      @Override
      public void serialize(Byte value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        ProtobufGenerator pgen = (ProtobufGenerator) gen;
        pgen.writeNumber((int) value);
      }
    };
  }

  @Override
  public JsonDeserializer<Byte> createDeserializer() {
    return new StdScalarDeserializer<Byte>(Byte.class) {

      @Override
      public Byte deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ProtobufParser pp = (ProtobufParser) p;
        return (byte) pp.getIntValue();
      }
    };
  }

  @Override
  public Builder getMessageElementBuilder() {
    return null;
  }

}
