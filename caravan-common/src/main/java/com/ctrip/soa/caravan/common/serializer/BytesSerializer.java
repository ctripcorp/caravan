package com.ctrip.soa.caravan.common.serializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface BytesSerializer extends Serializer {

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] is, Class<T> clazz);

}
