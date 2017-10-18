package com.ctrip.soa.caravan.protobuf.v2;

import com.ctrip.soa.caravan.protobuf.v2.ProtobufConfig.EnumMode;
import com.ctrip.soa.caravan.protobuf.v2.customization.array.ArrayProvider;
import com.ctrip.soa.caravan.protobuf.v2.customization.array.CustomSimpleDeserializers;
import com.ctrip.soa.caravan.protobuf.v2.customization.array.CustomSimpleSerializers;
import com.ctrip.soa.caravan.protobuf.v2.customization.map.MapCustomizationFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.serializer.SerializationException;
import com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.protobuf.v2.customization.CustomBeanSerializerFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.CustomProtobufFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.TypeCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.bigdecimal.BigDecimalCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.biginteger.BigIntegerCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.enums.EnumCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.scalar.BooleanCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.scalar.ByteCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.scalar.CharacterCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.scalar.ShortCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.schema.CustomProtobufSchemaGenerator;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.calendar.CalendarCustomizationFactory;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.xmlgregoriancalendar.XMLGregorianCalendarCustomizationFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;

public class ProtobufMapperWrapper {

    private ProtobufMapper _mapper;

    private ConcurrentHashMap<Class<?>, ProtobufSchema> _classSchemaMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class<?>, ObjectReader> _classObjectReaderMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class<?>, ObjectWriter> _classObjectWriterMap = new ConcurrentHashMap<>();

    private Map<Class<?>, DataType> customizedDataTypes = new HashMap<>();

    private TypeCustomizationFactory<?>[] typeCustomizationFactories;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ProtobufMapperWrapper(ProtobufConfig protobufConfig) {
        _mapper = new ProtobufMapper(new CustomProtobufFactory());
        _mapper.setSerializerFactory(new CustomBeanSerializerFactory(null));

        typeCustomizationFactories = new TypeCustomizationFactory[] {
            new ByteCustomizationFactory(),
            new ShortCustomizationFactory(),
            new BooleanCustomizationFactory(),
            new BigDecimalCustomizationFactory(_mapper),
            new XMLGregorianCalendarCustomizationFactory(_mapper),
            new BigIntegerCustomizationFactory(),
            new EnumCustomizationFactory(protobufConfig),
            new CalendarCustomizationFactory(_mapper),
            new CharacterCustomizationFactory(),
            new MapCustomizationFactory(_mapper),
        };

        ArrayProvider arrayProvider = arrayProvider();

        SimpleModule module = new SimpleModule();
        module.setSerializers(new CustomSimpleSerializers(arrayProvider));
        module.setDeserializers(new CustomSimpleDeserializers(arrayProvider));

        for (TypeCustomizationFactory factory : typeCustomizationFactories) {
            for (Class<?> clazz : factory.getTargetClasses()) {
                module.addSerializer(clazz, factory.createSerializer());
                module.addDeserializer(clazz, factory.createDeserializer());
                if (factory.getTargetProtobufDataType() != null) {
                    customizedDataTypes.put(clazz, factory.getTargetProtobufDataType());
                }
            }
        }
        _mapper.registerModule(module);
    }

    public ProtobufMapperWrapper() {
        this(defaultProtobufConfig());
    }

    private static ProtobufConfig defaultProtobufConfig() {
        ProtobufConfig protobufConfig = new ProtobufConfig();

        protobufConfig.setEnumMode(EnumMode.ByOrdinal);

        return protobufConfig;
    }

    private ArrayProvider arrayProvider() {
        return new ArrayProvider() {
            @SuppressWarnings("rawtypes")
            public TypeCustomizationFactory factoryForArrayOf(Class<?> elementClass) {
                for (final TypeCustomizationFactory factory : typeCustomizationFactories) {
                    boolean hasNonBytePrimitive = false;
                    for (Class<?> targetClass : factory.getTargetClasses()) {
                        if (targetClass.isPrimitive() && targetClass != byte.class) {
                            hasNonBytePrimitive = true;
                        }
                    }

                    if (hasNonBytePrimitive) {
                        for (Class<?> targetClass : factory.getTargetClasses()) {
                            if (elementClass == targetClass) {
                                return factory;
                            }
                        }
                    }
                }

                return null;
            }
        };
    }

    public void writeValue(OutputStream os, Object obj) {
        try {
            if (obj == null) {
                return;
            }

            ObjectWriter writer = getWriter(obj.getClass());
            writer.writeValue(os, obj);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    public <T> T readValue(InputStream is, Class<T> clazz) {
        NullArgumentChecker.DEFAULT.check(clazz, "clazz");
        try {
            ObjectReader reader = getReader(clazz);
            return reader.readValue(is);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    public byte[] writeValue(Object obj) {
        try {
            if (obj == null)
                return null;

            ObjectWriter writer = getWriter(obj.getClass());
            return writer.writeValueAsBytes(obj);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    public <T> T readValue(byte[] is, Class<T> clazz) {
        NullArgumentChecker.DEFAULT.check(clazz, "clazz");
        try {
            ObjectReader reader = getReader(clazz);
            return reader.readValue(is);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    protected ProtobufMapper mapper() {
        return _mapper;
    }

    protected ObjectReader getReader(Class<?> clazz) {
        ObjectReader reader = _classObjectReaderMap.get(clazz);
        if (reader == null) {
            ProtobufSchema schema = getSchema(clazz);
            reader = _mapper.readerFor(clazz).with(schema);
            _classObjectReaderMap.put(clazz, reader);
        }

        return reader;
    }

    protected ObjectWriter getWriter(Class<?> clazz) {
        ObjectWriter writer = _classObjectWriterMap.get(clazz);
        if (writer == null) {
            ProtobufSchema schema = getSchema(clazz);
            writer = _mapper.writerFor(clazz).with(schema);
            _classObjectWriterMap.put(clazz, writer);
        }

        return writer;
    }

    public ProtobufSchema getSchema(final Class<?> clazz) {
        return ConcurrentHashMapValues.getOrAddWithLock(_classSchemaMap, clazz, new Func<ProtobufSchema>() {
            @Override
            public ProtobufSchema execute() {
                try {
                    ProtobufSchemaGenerator gen = new CustomProtobufSchemaGenerator(customizedDataTypes, typeCustomizationFactories);
                    _mapper.acceptJsonFormatVisitor(clazz, gen);
                    return gen.getGeneratedSchema();
                } catch (JsonMappingException ex) {
                    throw new SerializationException(ex);
                }
            }
        });
    }

}
