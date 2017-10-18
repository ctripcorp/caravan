package com.ctrip.soa.caravan.protobuf.v2.customization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.util.NameTransformer;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by marsqing on 20/03/2017.
 */
public abstract class AbstractBeanSerializer extends BeanSerializerBase {

  private static final long serialVersionUID = -3618164443537292758L;

  public AbstractBeanSerializer(JavaType type, BeanSerializerBuilder builder,
      BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
    super(type, builder, properties, filteredProperties);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    // replace field of java class with message of protobuf
    bean = convertBeanToSerialize(bean);

    if(bean instanceof List) {
      for(Object ele : (List)bean) {
        gen.writeStartObject(bean);
        serializeFields(ele, gen, provider);
        gen.writeEndObject();
      }
    } else {
      gen.writeStartObject(bean);
      serializeFields(bean, gen, provider);
      gen.writeEndObject();
    }
  }

  protected abstract Object convertBeanToSerialize(Object bean);

  // belows are trivial methods

  @Override
  public JsonSerializer<Object> unwrappingSerializer(NameTransformer unwrapper) {
    return new UnwrappingBeanSerializer(this, unwrapper);
  }

  @Override
  public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
    return this;
  }

  @Override
  public BeanSerializerBase withFilterId(Object filterId) {
    return this;
  }

  @Override
  protected BeanSerializerBase withIgnorals(Set<String> toIgnore) {
    return this;
  }

  @Override
  protected BeanSerializerBase asArraySerializer() {
    if ((_objectIdWriter == null)
        && (_anyGetterWriter == null)
        && (_propertyFilterId == null)
        ) {
      return new BeanAsArraySerializer(this);
    }
    // already is one, so:
    return this;
  }

}
