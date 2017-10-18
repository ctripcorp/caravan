package com.ctrip.soa.caravan.protobuf.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import java.io.ByteArrayOutputStream;
import org.junit.Test;

/**
 * Created by marsqing on 14/06/2017.
 */
public class StringTest {

  @Test
  public void stringTest() throws Exception {
    Pojo13 p = new Pojo13();
    p.setValue1(makeString(7995));
    p.setValue2(makeString(7995));

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    ProtobufMapper mapper = new ProtobufMapper();
    mapper.writer(getSchema(mapper, p.getClass())).writeValue(bout, p);
  }

  private ProtobufSchema getSchema(ObjectMapper mapper, Class<?> clazz) throws Exception {
    ProtobufSchemaGenerator gen = new ProtobufSchemaGenerator();
    mapper.acceptJsonFormatVisitor(clazz, gen);
    return gen.getGeneratedSchema();
  }

  private String makeString(int len) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append("a");
    }
    return sb.toString();
  }

  public static class Pojo13 {

    private String value1;
    private String value2;

    public Pojo13() {
    }

    public String getValue1() {

      return value1;
    }

    public void setValue1(String value1) {
      this.value1 = value1;
    }

    public String getValue2() {
      return value2;
    }

    public void setValue2(String value2) {
      this.value2 = value2;
    }
  }

}


