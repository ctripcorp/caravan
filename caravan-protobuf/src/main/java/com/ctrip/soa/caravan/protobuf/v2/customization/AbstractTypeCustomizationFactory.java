package com.ctrip.soa.caravan.protobuf.v2.customization;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Created by marsqing on 21/03/2017.
 */
public abstract class AbstractTypeCustomizationFactory<T> implements TypeCustomizationFactory<T> {

  protected SerializerProvider serializerProvider;
  protected CustomBeanSerializerFactory serializerFactory;
  protected TypeFactory typeFactory;
  protected DeserializationContext deserializationContext;
  protected DeserializationConfig deserializationConfig;

  public AbstractTypeCustomizationFactory(ObjectMapper mapper) {
    this.serializerProvider = mapper.getSerializerProviderInstance();
    this.serializerFactory = (CustomBeanSerializerFactory) mapper.getSerializerFactory();
    this.typeFactory = mapper.getTypeFactory();

    this.deserializationContext = mapper.getDeserializationContext();
    this.deserializationConfig = mapper.getDeserializationConfig();
  }

  public BuilderAndBeanPropertiyWriters constructBeanSerializerBuilder(JavaType type) {
    BeanDescription beanDesc = serializerProvider.getConfig().introspect(type);
    BeanSerializerBuilder builder = new BeanSerializerBuilder(beanDesc);
    BeanPropertyWriter[] properties;

    try {
      properties = serializerFactory.findBeanProperties(serializerProvider, beanDesc, builder).toArray(new BeanPropertyWriter[0]);
    } catch (JsonMappingException e) {
      throw new RuntimeException("Unexpected exception", e);
    }

    return new BuilderAndBeanPropertiyWriters(builder, properties);
  }

  protected BuilderAndPropertyMap constructBeanDeserializerBuilder(JavaType type) {
    BeanDescription beanDesc = serializerProvider.getConfig().introspect(type);

    BeanDeserializerBuilder builder;

    CustomBeanDeserializerFactory factory = CustomBeanDeserializerFactory.instance;

    DeserializationContext ctxt = ((DefaultDeserializationContext) deserializationContext).copy();

    try {
      Field field = DeserializationContext.class.getDeclaredField("_config");
      field.setAccessible(true);
      field.set(ctxt, deserializationConfig);

      // copy from factory.buildBeanDeserializer()
      ValueInstantiator valueInstantiator = factory.findValueInstantiator(ctxt, beanDesc);
      builder = new BeanDeserializerBuilder(beanDesc, ctxt);
      builder.setValueInstantiator(valueInstantiator);
      factory.addBeanProps(ctxt, beanDesc, builder);
      factory.addObjectIdReader(ctxt, beanDesc, builder);
      factory.addBackReferenceProperties(ctxt, beanDesc, builder);
      factory.addInjectables(ctxt, beanDesc, builder);
    } catch (Exception e) {
      throw new RuntimeException("Unexpected exception", e);
    }

    List<SettableBeanProperty> properties = new ArrayList<>();
    Iterator<SettableBeanProperty> propertiesIter = builder.getProperties();
    while (propertiesIter.hasNext()) {
      properties.add(propertiesIter.next());
    }

    BeanPropertyMap propertyMap = new BeanPropertyMap(false, properties, Collections.<String, List<PropertyName>>emptyMap());

    return new BuilderAndPropertyMap(builder, propertyMap, beanDesc);
  }

  public static class BuilderAndBeanPropertiyWriters {

    public BeanSerializerBuilder builder;
    public BeanPropertyWriter[] properties;

    public BuilderAndBeanPropertiyWriters(BeanSerializerBuilder builder, BeanPropertyWriter[] properties) {
      this.builder = builder;
      this.properties = properties;
    }
  }

  public static class BuilderAndPropertyMap {

    public BeanDeserializerBuilder builder;
    public BeanPropertyMap propertyMap;
    public BeanDescription beanDesc;

    public BuilderAndPropertyMap(BeanDeserializerBuilder builder, BeanPropertyMap propertyMap, BeanDescription beanDesc) {
      this.builder = builder;
      this.propertyMap = propertyMap;
      this.beanDesc = beanDesc;
    }
  }
}

