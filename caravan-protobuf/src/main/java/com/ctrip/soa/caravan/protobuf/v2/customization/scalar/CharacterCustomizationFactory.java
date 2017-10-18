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
 * @author chennan
 */
public class CharacterCustomizationFactory implements TypeCustomizationFactory<Character> {

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        char.class,
        Character.class
    };
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return ScalarType.UINT32;
  }

  @SuppressWarnings("serial")
  @Override
  public JsonSerializer<Character> createSerializer() {
    return new StdScalarSerializer<Character>(Character.class) {
      @Override
      public void serialize(Character value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        ProtobufGenerator pgen = (ProtobufGenerator) gen;
        pgen.writeNumber(value);
      }
    };
  }

  @SuppressWarnings("serial")
  @Override
  public JsonDeserializer<Character> createDeserializer() {
    return new StdScalarDeserializer<Character>(Character.class) {

      @Override
      public Character deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ProtobufParser pp = (ProtobufParser) p;
        return (char) pp.getIntValue();
      }
    };
  }

  @Override
  public Builder getMessageElementBuilder() {
    return null;
  }
}
