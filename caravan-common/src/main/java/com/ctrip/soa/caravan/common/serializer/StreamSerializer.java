package com.ctrip.soa.caravan.common.serializer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface StreamSerializer extends Serializer {

    void serialize(OutputStream os, Object obj);

    <T> T deserialize(InputStream is, Class<T> clazz);

}
