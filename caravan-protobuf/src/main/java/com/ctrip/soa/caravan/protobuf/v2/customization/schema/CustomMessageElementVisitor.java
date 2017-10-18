package com.ctrip.soa.caravan.protobuf.v2.customization.schema;

import com.ctrip.soa.caravan.protobuf.v2.customization.enums.EnumCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.map.DynamicClassFactory;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType.NamedType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType.ScalarType;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.FieldElement;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.FieldElement.Label;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.DefinedTypeElementBuilders;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.MessageElementVisitor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marsqing on 15/03/2017.
 */
public class CustomMessageElementVisitor extends MessageElementVisitor {

  private Map<Class<?>, DataType> customizedDataTypes = new HashMap<>();

  public CustomMessageElementVisitor(SerializerProvider provider, JavaType type,
      DefinedTypeElementBuilders definedTypeElementBuilders, boolean isNested, Map<Class<?>, DataType> customizedDataTypes) {
    super(provider, type, definedTypeElementBuilders, isNested);

    this.customizedDataTypes = customizedDataTypes;
    // so we can get Enum class in EnumCustomizationFactory's deserializer
    _builder.name(type.getRawClass().getName());
  }

  @Override
  protected DataType getDataType(JavaType type) throws JsonMappingException {
    // 1. customized types
    if (customizedDataTypes.containsKey(type.getRawClass())) {
      return customizedDataTypes.get(type.getRawClass());
    }

    // 2. wrapper of primitive types
    Class<?> rawClass = type.getRawClass();
    if (rawClass == Integer.class) {
      return ScalarType.INT32;
    }
    if (rawClass == Long.class) {
      return ScalarType.INT64;
    }
    if (rawClass == Float.class) {
      return ScalarType.FLOAT;
    }
    if (rawClass == Double.class) {
      return ScalarType.DOUBLE;
    }

    // 3. Enum
    if (rawClass.isEnum()) {
      return EnumCustomizationFactory.DATA_TYPE;
    }

    return innerGetDataType(type);
  }

  // Copy from MessageElementVisitor.getDataType(JavaType type).
  // Except
  // 1. Replace ProtoBufSchemaVisitor with CtripProtoBufSchemaVisitor
  // 2. Use NamedType.create(type.getRawClass().getName()) instead of getSimple() to help determine enum class
  private DataType innerGetDataType(JavaType type) throws JsonMappingException {
    if (!_definedTypeElementBuilders.containsBuilderFor(type)) { // No self ref
      if (isNested(type)) {
        if (!_nestedTypes.contains(type)) { // create nested type
          _nestedTypes.add(type);
          CustomProtoBufSchemaVisitor builder = acceptTypeElement(_provider, type,
              _definedTypeElementBuilders, true);
          DataType scalarType = builder.getSimpleType();
          if (scalarType != null) {
            return scalarType;
          }
          _builder.addType(builder.build());
        }
      } else { // track non-nested types to generate them later
        CustomProtoBufSchemaVisitor builder = acceptTypeElement(_provider, type,
            _definedTypeElementBuilders, false);
        DataType scalarType = builder.getSimpleType();
        if (scalarType != null) {
          return scalarType;
        }
      }
    }

    return NamedType.create(type.getRawClass().getName());
  }

  private CustomProtoBufSchemaVisitor acceptTypeElement(SerializerProvider provider, JavaType type,
      DefinedTypeElementBuilders definedTypeElementBuilders, boolean isNested) throws JsonMappingException {
    JsonSerializer<Object> serializer = provider.findValueSerializer(type, null);
    CustomProtoBufSchemaVisitor visitor = new CustomProtoBufSchemaVisitor(provider, definedTypeElementBuilders, isNested, customizedDataTypes);
    serializer.acceptJsonFormatVisitor(visitor, type);
    return visitor;
  }

  private boolean isNested(JavaType type) {
    Class<?> match = type.getRawClass();
    for (Class<?> cls : _type.getRawClass().getDeclaredClasses()) {
      if (cls == match) {
        return true;
      }
    }
    return false;
  }

  protected FieldElement buildFieldElement(BeanProperty writer, Label label) throws JsonMappingException {
    FieldElement.Builder fBuilder = FieldElement.builder();

    fBuilder.name(writer.getName());
    fBuilder.tag(nextTag(writer));

    JavaType type = writer.getType();

    if (type.isArrayType() && type.getContentType().getRawClass() == byte.class) {
      fBuilder.label(label);
      fBuilder.type(ScalarType.BYTES);
    } else if (type.isArrayType() || type.isCollectionLikeType()) {
      fBuilder.label(Label.REPEATED);
      fBuilder.type(getDataType(type.getContentType()));
    } else if (type instanceof MapType) {
      Class<?> wrapperClass = DynamicClassFactory.INSTANCE.fetchOrCreatePairClass((MapType) type);

      fBuilder.label(Label.REPEATED);
      fBuilder.type(getDataType(SimpleType.constructUnsafe(wrapperClass)));
    } else {
      fBuilder.label(label);
      fBuilder.type(getDataType(type));
    }
    return fBuilder.build();
  }


}
