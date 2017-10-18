package com.ctrip.soa.caravan.protobuf.v2.customization;

import com.ctrip.soa.caravan.common.collect.KeyValuePair;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufField;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufMessage;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by marsqing on 24/04/2017.
 */
public class ProtobufParserUtil {

  private static ConcurrentMap<KeyValuePair<String, String>, Field> fieldCache = new ConcurrentHashMap<>();

  @SuppressWarnings("rawtypes")
  public static Class getEnumClassOfCurrentField(ProtobufMessage curMsg, ProtobufField curField) {
    Field javaField = getCurrentField(curMsg, curField);
    Class<?> fieldClass = javaField.getType();

    // plain enum
    if (fieldClass.isEnum()) {
      return fieldClass;
    }

    // array of enum
    if (fieldClass.isArray() && fieldClass.getComponentType().isEnum()) {
      return fieldClass.getComponentType();
    }

    // collection of enum
    if (javaField.getGenericType() instanceof ParameterizedType) {
      ParameterizedType genericType = (ParameterizedType) javaField.getGenericType();
      Type[] actualArguments = genericType.getActualTypeArguments();
      if (actualArguments != null) {
        // List/Set/Map at most 2 type arguments
        for (int i = 0; i < 2 && i < actualArguments.length; i++) {
          if (actualArguments[i] instanceof Class) {
            Class<?> genericClass = (Class<?>) actualArguments[i];
            if (genericClass.isEnum()) {
              return genericClass;
            }
          }
        }
      }
    }

    throw new RuntimeException(String.format("Field %s of Class %s is not enum or collection of enum", javaField.getName(), javaField.getDeclaringClass()));
  }

  public static Field getCurrentField(ProtobufMessage curMsg, ProtobufField curField) {
    String enclosingClass = curMsg.getName();
    String fieldName = curField.name;

    KeyValuePair<String, String> key = new KeyValuePair<>(enclosingClass, fieldName);
    Field javaField = fieldCache.get(key);

    if (javaField != null) {
      return javaField;
    }

    Class<?> clazz;
    try {
      clazz = Class.forName(enclosingClass);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(curMsg.getName() + " is not a valid java class name", e);
    }

    try {
      javaField = clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      try {
        javaField = clazz.getDeclaredField(changeFirstLetterCase(fieldName));
      } catch (NoSuchFieldException e2) {
        throw new RuntimeException(String.format("Field %s not found in class %s", fieldName, enclosingClass), e);
      }
    }

    fieldCache.put(key, javaField);

    return javaField;
  }

  private static String changeFirstLetterCase(String fieldName) {
    String firstLetter = fieldName.substring(0, 1);

    if (firstLetter.toUpperCase().equals(firstLetter)) {
      return firstLetter.toLowerCase() + fieldName.substring(1);
    } else {
      return firstLetter.toUpperCase() + fieldName.substring(1);
    }

  }

}
