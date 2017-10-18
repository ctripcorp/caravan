package com.ctrip.soa.caravan.protobuf.v2.customization;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import java.util.List;

/**
 * Created by marsqing on 20/03/2017.
 */
public class CustomBeanSerializerFactory extends BeanSerializerFactory {

  private static final long serialVersionUID = 1L;

  public CustomBeanSerializerFactory(SerializerFactoryConfig config) {
    super(config);
  }

  // make it public
  @Override
  public List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov,
      BeanDescription beanDesc, BeanSerializerBuilder builder)
      throws JsonMappingException {
    return super.findBeanProperties(prov, beanDesc, builder);
  }

  @Override
  public SerializerFactory withConfig(SerializerFactoryConfig config)
  {
    if (_factoryConfig == config) {
      return this;
    }
    if (getClass() != CustomBeanSerializerFactory.class) {
      throw new IllegalStateException("Subtype of BeanSerializerFactory ("+getClass().getName()
          +") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with "
          +"additional serializer definitions");
    }
    return new CustomBeanSerializerFactory(config);
  }
}
