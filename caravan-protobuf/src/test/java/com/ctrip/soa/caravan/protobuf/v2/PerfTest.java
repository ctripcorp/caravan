package com.ctrip.soa.caravan.protobuf.v2;

import java.io.ByteArrayOutputStream;
import org.junit.Test;

/**
 * Created by marsqing on 25/04/2017.
 */
public class PerfTest {

  @Test
  public void test() throws InterruptedException {

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      sb.append("A");
    }

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    Pojo p = new Pojo();
    p.setStr(sb.toString());

    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    for (int i = 0; i < 10000; i++) {
      JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);
    }

    long start = System.currentTimeMillis();
    for (int i = 0; i < 1000; i++) {
      JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);
    }
    System.out.println(System.currentTimeMillis() - start);

  }

  public static class Pojo {

    private String str;

    public String getStr() {
      return str;
    }

    public void setStr(String str) {
      this.str = str;
    }
  }

}
