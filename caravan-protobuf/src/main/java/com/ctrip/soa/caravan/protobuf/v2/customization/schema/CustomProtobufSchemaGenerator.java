package com.ctrip.soa.caravan.protobuf.v2.customization.schema;

import com.ctrip.soa.caravan.protobuf.v2.customization.TypeCustomizationFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.TypeElement;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.MessageElementVisitor;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.TypeElementBuilder;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by marsqing on 15/03/2017.
 */
@SuppressWarnings("rawtypes")
public class CustomProtobufSchemaGenerator extends ProtobufSchemaGenerator {

private TypeCustomizationFactory[] typeCustomizationFactories;
  private Map<Class<?>, DataType> customizedDataTypes = new HashMap<>();

  public CustomProtobufSchemaGenerator(Map<Class<?>, DataType> customizedDataTypes, TypeCustomizationFactory[] typeCustomizationFactories) {
    this.customizedDataTypes = customizedDataTypes;
    this.typeCustomizationFactories = typeCustomizationFactories;
  }

  @Override
  public JsonObjectFormatVisitor expectObjectFormat(JavaType type) {
    // from ProtobufSchemaGenerator
    _rootType = type;

    MessageElementVisitor visitor = new CustomMessageElementVisitor(_provider, type, _definedTypeElementBuilders, _isNested, customizedDataTypes);
    _builder = visitor;
    _definedTypeElementBuilders.addTypeElement(type, visitor, _isNested);

    return visitor;
  }

  @Override
  public Set<TypeElement> buildWithDependencies() {
    Set<TypeElement> allTypeElements = new LinkedHashSet<>();
    allTypeElements.add(build());

    for (TypeElementBuilder builder : _definedTypeElementBuilders.getDependencyBuilders()) {
      allTypeElements.add(builder.build());
    }

    for (TypeCustomizationFactory factory : typeCustomizationFactories) {
      if (factory.getMessageElementBuilder() != null) {
        allTypeElements.add(factory.getMessageElementBuilder().build());
      }
    }
    return allTypeElements;
  }

}
