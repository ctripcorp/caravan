package com.ctrip.soa.caravan.protobuf.v2;


import com.ctrip.soa.caravan.protobuf.v2.JacksonProtobuf2Serializer;
import com.ctrip.soa.caravan.protobuf.v2.Pojo2.Embed;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by marsqing on 17/03/2017.
 */
public class SomeTest {

  @org.junit.Test
  public void test() {
    System.out.println(new ProtobufMapperWrapper().getSchema(Pojo2.class).getSource());

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    Pojo2 pojo = new Pojo2();
    pojo.setI(1);
    pojo.setJ(4);
    Embed pe = new Embed();
    pe.setA(2);
    pe.setB(3);
    pojo.setPe(pe);

    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, pojo);

    showBytes(bout.toByteArray());

  }

  @org.junit.Test
  public void test2() {
    BigInteger bi = new BigInteger("1" + Long.toString(Long.MAX_VALUE));
    showBytes(bi.toByteArray());
  }

  private void showBytes(byte[] bytes) {
    for (byte b : bytes) {
      System.out.print(String.format("%8s", Integer.toHexString(b)).substring(6, 8).replaceAll(" ", "0") + " ");
    }
    System.out.println();
  }

  @org.junit.Test
  public void test3() throws Exception {
    ProtobufMapper mapper = new ProtobufMapper();
    ProtobufSchemaGenerator gen = new ProtobufSchemaGenerator();
    mapper.acceptJsonFormatVisitor(BigDecimalPojo.class, gen);
    System.out.println(gen.getGeneratedSchema().getSource());
  }

  public static class BigDecimalPojo {

    private Date bigDecimal;

    public Date getBigDecimal() {
      return bigDecimal;
    }

    public void setBigDecimal(Date bigDecimal) {
      this.bigDecimal = bigDecimal;
    }
  }

}
