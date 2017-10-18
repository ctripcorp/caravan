package com.ctrip.soa.caravan.protobuf.v2.customization;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;

/**
 * Created by marsqing on 20/03/2017.
 */
public class CustomBeanDeserializerFactory extends BeanDeserializerFactory {

  private static final long serialVersionUID = 1L;

  public final static CustomBeanDeserializerFactory instance = new CustomBeanDeserializerFactory(
      new DeserializerFactoryConfig());

  public CustomBeanDeserializerFactory(DeserializerFactoryConfig config) {
    super(config);
  }

  @Override
  public void addBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
    super.addBeanProps(ctxt, beanDesc, builder);
  }

  @Override
  public void addObjectIdReader(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
    super.addObjectIdReader(ctxt, beanDesc, builder);
  }

  @Override
  public void addBackReferenceProperties(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
    super.addBackReferenceProperties(ctxt, beanDesc, builder);
  }

  @Override
  public void addInjectables(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
    super.addInjectables(ctxt, beanDesc, builder);
  }
}
