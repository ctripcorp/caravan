package com.ctrip.soa.caravan.util.serializer.filterable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
class FilterableBeanSerializerModifier extends BeanSerializerModifier {

    private static final Logger _logger = LoggerFactory.getLogger(FilterableBeanSerializerModifier.class);

    private FilterableJsonSerializerConfig _config;
    private ConcurrentHashMap<JavaType, JsonSerializer<Object>> _customSerializers = new ConcurrentHashMap<>();

    public FilterableBeanSerializerModifier(FilterableJsonSerializerConfig config) {
        NullArgumentChecker.DEFAULT.check(config, "config");
        _config = config;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        FilterableType filterableType = _config.getFilterableType(beanDesc.getBeanClass());
        if (filterableType == null)
            return beanProperties;

        for (final BeanPropertyWriter beanPropertyWriter : beanProperties) {
            final FilterableProperty property = filterableType.getProperty(beanPropertyWriter.getName());
            if (property == null || property.getSerializationFilter() == null)
                continue;

            beanPropertyWriter.assignSerializer(new StdSerializer<Object>(Object.class) {

                private static final long serialVersionUID = 1L;

                @Override
                public void serialize(Object value, JsonGenerator gen, final SerializerProvider provider) throws IOException {
                    JsonSerializer<Object> serializer = ConcurrentHashMapValues.getOrAdd(_customSerializers, beanPropertyWriter.getType(),
                            new Func<JsonSerializer<Object>>() {
                                @Override
                                public JsonSerializer<Object> execute() {
                                    try {
                                        return BeanSerializerFactory.instance.createSerializer(provider, beanPropertyWriter.getType());
                                    } catch (Throwable ex) {
                                        _logger.error("Error occurred when creating custom serializer for type: " + beanPropertyWriter.getType(), ex);
                                        return NullSerializer.instance;
                                    }
                                }
                            });

                    value = property.getSerializationFilter().filter(property, value);
                    if (value == null) {
                        gen.writeNull();
                        return;
                    }

                    serializer.serialize(value, gen, provider);
                }
            });

            _logger.info("Will apply filter {} for property: {}.", property.getSerializationFilter(), property.name());
        }

        return beanProperties;
    }
}
