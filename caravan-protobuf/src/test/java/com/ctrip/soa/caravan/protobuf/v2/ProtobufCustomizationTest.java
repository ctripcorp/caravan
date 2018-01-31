package com.ctrip.soa.caravan.protobuf.v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.Test;
import org.junit.Assert;

import com.ctrip.soa.caravan.protobuf.v2.JacksonProtobuf2Serializer;

/**
 * Created by marsqing on 15/03/2017.
 */
public class ProtobufCustomizationTest {

  @Test
  public void testDate() throws Exception {
    Date value = new Date();
    PojoDate origin = new PojoDate();
    origin.setpDate(value);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, origin);
    PojoDate got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), PojoDate.class);

    Assert.assertEquals(value, got.getpDate());
  }

  @Test
  public void testBigDecimal() throws Exception {
    String value = "12345678901234567890123456789";
    PojoBigDecimal origin = new PojoBigDecimal();
    origin.setpBigDecimal(new BigDecimal(value));

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, origin);
    PojoBigDecimal got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), PojoBigDecimal.class);

    Assert.assertEquals(value, got.getpBigDecimal().toString());
  }

}
