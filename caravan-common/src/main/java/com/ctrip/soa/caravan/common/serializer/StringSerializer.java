package com.ctrip.soa.caravan.common.serializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface StringSerializer extends Serializer {

    String serialize(Object obj);

    <T> T deserialize(String s, Class<T> clazz);

}
