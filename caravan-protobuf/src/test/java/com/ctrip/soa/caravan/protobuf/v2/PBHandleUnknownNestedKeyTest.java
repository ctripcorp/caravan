package com.ctrip.soa.caravan.protobuf.v2;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.Test;

public class PBHandleUnknownNestedKeyTest {

  @Test
  public void test() throws Exception {
    NestedTwoField nestedTwoField = new NestedTwoField();
    nestedTwoField.setNested1(1);
    nestedTwoField.setNested2(2);
    MoreNestedField moreNestedField = new MoreNestedField();
    moreNestedField.setF1(nestedTwoField);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(out, moreNestedField);

    LessNestedField lessNestedField = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(out.toByteArray()), LessNestedField.class);

    assertEquals(moreNestedField.getF1().getNested2(), lessNestedField.getF1().getNested2());
  }

  public static class LessNestedField {

    @JsonProperty(value = "f1", index = 1)
    private NestedOneField f1;

    public NestedOneField getF1() {
      return f1;
    }

    public void setF1(NestedOneField f1) {
      this.f1 = f1;
    }
  }

  public static class MoreNestedField {

    @JsonProperty(value = "f1", index = 1)
    private NestedTwoField f1;

    public NestedTwoField getF1() {
      return f1;
    }

    public void setF1(NestedTwoField f1) {
      this.f1 = f1;
    }
  }

  public static class NestedOneField {

    @JsonProperty(value = "nested2", index = 2)
    private int nested2;

    public int getNested2() {
      return nested2;
    }

    public void setNested2(int nested2) {
      this.nested2 = nested2;
    }
  }

  public static class NestedTwoField {

    @JsonProperty(value = "nested1", index = 1)
    private int nested1;

    @JsonProperty(value = "nested2", index = 2)
    private int nested2;

    public int getNested1() {
      return nested1;
    }

    public void setNested1(int nested1) {
      this.nested1 = nested1;
    }

    public int getNested2() {
      return nested2;
    }

    public void setNested2(int nested2) {
      this.nested2 = nested2;
    }
  }

}
