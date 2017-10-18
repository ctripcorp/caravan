package com.ctrip.soa.caravan.protobuf.v2.customization;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.MessageElement;

/**
 * Created by marsqing on 17/03/2017.
 */
public interface TypeCustomizationFactory<T> {

  Class<?>[] getTargetClasses();

  DataType getTargetProtobufDataType();

  MessageElement.Builder getMessageElementBuilder();

  JsonSerializer<T> createSerializer();

  JsonDeserializer<T> createDeserializer();

}
