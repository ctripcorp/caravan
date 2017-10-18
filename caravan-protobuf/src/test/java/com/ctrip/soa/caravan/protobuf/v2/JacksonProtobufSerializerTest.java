package com.ctrip.soa.caravan.protobuf.v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.ctrip.soa.caravan.protobuf.SimplePojo;
import com.ctrip.soa.caravan.protobuf.SimplePojo2;
import com.ctrip.soa.caravan.protobuf.v2.JacksonProtobuf2Serializer;

public class JacksonProtobufSerializerTest {

    @Test
    public void SerializePojo01() throws Exception {
        SimplePojo obj = SimplePojo.SAMPLE;
        SimplePojo obj2 = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            JacksonProtobuf2Serializer.INSTANCE.serialize(os, obj);
            try (ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray())) {
                obj2 = JacksonProtobuf2Serializer.INSTANCE.deserialize(is, SimplePojo.class);
            }
        }

        Assert.assertNotNull(obj2);
        Assert.assertNotEquals(obj, obj2);
    }

    @Test
    @Ignore
    public void SerializePojo02() throws Exception {
        SimplePojo2 obj = SimplePojo2.SAMPLE;
        SimplePojo2 obj2 = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            JacksonProtobuf2Serializer.INSTANCE.serialize(os, obj);
            try (ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray())) {
                obj2 = JacksonProtobuf2Serializer.INSTANCE.deserialize(is, SimplePojo2.class);
            }
        }

        Assert.assertNotNull(obj2);
        Assert.assertNotEquals(obj, obj2);
    }

}
