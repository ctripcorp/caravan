package com.ctrip.soa.caravan.util.serializer;

import com.ctrip.soa.caravan.common.serializer.BytesSerializer;
import com.ctrip.soa.caravan.common.serializer.SerializationException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class JacksonJsonBytesSerializer implements BytesSerializer {

    public static final JacksonJsonBytesSerializer INSTANCE = new JacksonJsonBytesSerializer();

    private ObjectMapper _mapper = new ObjectMapper();

    private JacksonJsonBytesSerializer() {
        _mapper.configure(Feature.AUTO_CLOSE_TARGET, false);
        _mapper.configure(Feature.IGNORE_UNKNOWN, true);
        _mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        _mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        _mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        _mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        _mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        _mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public byte[] serialize(Object obj) {
        try {
            return _mapper.writeValueAsBytes(obj);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public <T> T deserialize(byte[] is, Class<T> clazz) {
        try {
            return _mapper.readValue(is, clazz);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

}
