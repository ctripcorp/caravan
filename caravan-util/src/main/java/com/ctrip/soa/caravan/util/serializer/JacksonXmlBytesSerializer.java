package com.ctrip.soa.caravan.util.serializer;

import com.ctrip.soa.caravan.common.serializer.BytesSerializer;
import com.ctrip.soa.caravan.common.serializer.SerializationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class JacksonXmlBytesSerializer implements BytesSerializer {

    public static final JacksonXmlBytesSerializer INSTANCE = new JacksonXmlBytesSerializer();

    private XmlMapper _mapper = new XmlMapper();

    private JacksonXmlBytesSerializer() {
        _mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        _mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        _mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public String contentType() {
        return "application/xml";
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
