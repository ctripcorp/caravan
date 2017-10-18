package com.ctrip.soa.caravan.protobuf.v2.customization.schema;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.DefinedTypeElementBuilders;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.MessageElementVisitor;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtoBufSchemaVisitor;
import java.util.Map;

/**
 * Created by marsqing on 22/03/2017.
 */
public class CustomProtoBufSchemaVisitor extends ProtoBufSchemaVisitor {

  private Map<Class<?>, DataType> customizedDataTypes;

  public CustomProtoBufSchemaVisitor(SerializerProvider provider,
      DefinedTypeElementBuilders definedTypeElementBuilders, boolean isNested,
      Map<Class<?>, DataType> customizedDataTypes) {
    super(provider, definedTypeElementBuilders, isNested);

    this.customizedDataTypes = customizedDataTypes;
  }

  @Override
  public JsonObjectFormatVisitor expectObjectFormat(JavaType type) {
    // from ProtoBufSchemaVisitor

    MessageElementVisitor visitor = new CustomMessageElementVisitor(_provider, type, _definedTypeElementBuilders, _isNested, customizedDataTypes);
    _builder = visitor;
    _definedTypeElementBuilders.addTypeElement(type, visitor, _isNested);
    return visitor;
  }

}
