package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.duration;

import com.ctrip.soa.caravan.protobuf.v2.customization.TypeCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.TimeRelatedConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType.ScalarType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement.Builder;
import java.io.IOException;
import javax.xml.datatype.Duration;

/**
 * Created by marsqing on 24/03/2017.
 * Classes of javax.xml package will be specially treated by OptionalHandlerFactory.
 * A ToStringSerializer will be given.
 */
@SuppressWarnings("serial")
public class DurationCustomizationFactory implements TypeCustomizationFactory<Duration> {

  @Override
  public Class<?>[] getTargetClasses() {
    return new Class<?>[]{
        Duration.class
    };
  }

  @Override
  public DataType getTargetProtobufDataType() {
    return ScalarType.STRING;
  }

  @Override
  public Builder getMessageElementBuilder() {
    return null;
  }

  @Override
  public JsonSerializer<Duration> createSerializer() {
    return new StdScalarSerializer<Duration>(Duration.class) {

      @Override
      public void serialize(Duration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // TODO .net TimeSpan to string logic is not clear yet
        gen.writeString("1");
      }
    };
  }

  @Override
  public JsonDeserializer<Duration> createDeserializer() {
    return new StdScalarDeserializer<Duration>(Duration.class) {

      @Override
      public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // TODO
        p.getText();
        return TimeRelatedConverter.dataTypeFactory.newDuration(1);
      }
    };
  }
}
