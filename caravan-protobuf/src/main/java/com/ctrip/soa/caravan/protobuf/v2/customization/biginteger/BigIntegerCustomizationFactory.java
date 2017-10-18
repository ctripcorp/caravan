package com.ctrip.soa.caravan.protobuf.v2.customization.biginteger;

import com.ctrip.soa.caravan.protobuf.v2.customization.TypeCustomizationFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufGenerator;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType.ScalarType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement.Builder;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Serializer/Deserializer for ulong(csharp)'s counterpart BigInteger.
 * Note that only value below between [0, 18446744073709551615(max ulong value in csharp)] is supported.
 *
 * Created by marsqing on 21/03/2017.
 */
@SuppressWarnings("serial")
public class BigIntegerCustomizationFactory implements TypeCustomizationFactory<BigInteger> {

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        BigInteger.class
    };
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return ScalarType.UINT64;
  }

  @Override
  public Builder getMessageElementBuilder() {
    return null;
  }

  @Override
  public JsonSerializer<BigInteger> createSerializer() {
    return new StdScalarSerializer<BigInteger>(BigInteger.class) {
      @Override
      public void serialize(BigInteger value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        ProtobufGenerator pgen = (ProtobufGenerator) gen;

        // Just write. If value is bigger than Long.MAX_VALUE then the sign bit will match ulong.
        pgen.writeNumber(value.longValue());
      }
    };
  }

  @Override
  public JsonDeserializer<BigInteger> createDeserializer() {
    return new StdDeserializer<BigInteger>(BigInteger.class) {
      @Override
      public BigInteger deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // longValue's bits are same as ulong
        long longValue = p.getLongValue();

        // dump bits and parse it as ulong, effectively parse it as ulong
        return new BigInteger(Long.toHexString(longValue), 16);
      }
    };
  }
}
