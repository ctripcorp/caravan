package com.ctrip.soa.caravan.protobuf.v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.Test;

import com.ctrip.soa.caravan.protobuf.v2.JacksonProtobuf2Serializer;

/**
 * Created by marsqing on 15/03/2017.
 */
public class ProtobufCustomizationTest {

  @Test
  public void testDate() throws Exception {
    PojoDate origin = new PojoDate();
    origin.setpDate(new Date());

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, origin);
    PojoDate got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), PojoDate.class);

    System.out.println(got.getpDate());
  }


  @Test
  public void testBigDecimal() throws Exception {
    PojoBigDecimal origin = new PojoBigDecimal();
    origin.setpBigDecimal(new BigDecimal("1234567890123456789012345678901234567890"));

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, origin);
    PojoBigDecimal got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), PojoBigDecimal.class);

    System.out.println(got.getpBigDecimal());
  }

}
