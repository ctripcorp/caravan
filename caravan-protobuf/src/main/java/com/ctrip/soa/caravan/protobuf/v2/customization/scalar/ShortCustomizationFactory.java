package com.ctrip.soa.caravan.protobuf.v2.customization.scalar;

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
import java.io.IOException;

/**
 * Created by marsqing on 17/03/2017.
 */
@SuppressWarnings("serial")
public class ShortCustomizationFactory implements TypeCustomizationFactory<Short> {

  // .NET ProtoWriter.WriteInt16((short)value, dest);

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        short.class,
        Short.class
    };
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return ScalarType.INT32;
  }

  @Override
  public JsonSerializer<Short> createSerializer() {
    return new StdScalarSerializer<Short>(Short.class) {

      @Override
      public void serialize(Short value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        ProtobufGenerator pgen = (ProtobufGenerator) gen;
        pgen.writeNumber((int) value);
      }
    };
  }

  @Override
  public JsonDeserializer<Short> createDeserializer() {
    return new StdScalarDeserializer<Short>(Short.class) {

      @Override
      public Short deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ProtobufParser pp = (ProtobufParser) p;
        return (short) pp.getIntValue();
      }
    };
  }

  @Override
  public Builder getMessageElementBuilder() {
    return null;
  }

}
