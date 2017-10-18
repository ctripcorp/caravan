package com.ctrip.soa.caravan.protobuf.v2;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Created by marsqing on 23/03/2017.
 */
public class BugReport {

  @Test
  public void test() throws Exception {
    ProtobufMapper mapper = new ProtobufMapper();
    ProtobufSchema schema = getSchema(mapper, Level1.class);

    System.out.println(schema.getSource());

    Level1 level1 = new Level1();
    Level2 level2 = new Level2();
    Level3 level3a = new Level3();
    Level3 level3b = new Level3();

//    level1.setValue(1);
    level2.setValue(2);
    level3a.setValue(3);
    level3b.setValue(4);
    List<Level3> level3s = Arrays.asList(level3a, level3b);

    level1.setLevel2(level2);
    level2.setLevel3s(level3s);

    ByteArrayOutputStream bout = new ByteArrayOutputStream() {
      @Override
      public synchronized void write(byte[] b, int off, int len) {
        for (int i = 0; i < len; i++) {
          System.out.print(String.format("%8s", Integer.toHexString(b[off + i])).substring(6, 8).replaceAll(" ", "0") + " ");
        }
        System.out.println();

        super.write(b, off, len);
      }
    };
    mapper.writer(schema).writeValue(bout, level1);

    showBytes(bout.toByteArray());

    Level1 gotLevel1 = mapper.readerFor(Level1.class).with(schema).readValue(new ByteArrayInputStream(bout.toByteArray()));

//    byte[] correct = new byte[]{0x08, 0x01, 0x12, 0x0a, 0x08, 0x02, 0x12, 0x02, 0x08, 0x03, 0x12, 0x02, 0x08, 0x04};
//    Level1 gotLevel1 = mapper.readerFor(Level1.class).with(schema).readValue(new ByteArrayInputStream(correct));

//    assertEquals(level1.getValue(), gotLevel1.getValue());
    assertEquals(level2.getValue(), gotLevel1.getLevel2().getValue());
    assertEquals(level3s.size(), gotLevel1.getLevel2().getLevel3s().size());
    assertEquals(level3a.getValue(), gotLevel1.getLevel2().getLevel3s().get(0).getValue());
    assertEquals(level3b.getValue(), gotLevel1.getLevel2().getLevel3s().get(1).getValue());

//    assertEquals(level2.getValue(), gotLevel2.getValue());
//    assertEquals(level3s.size(), gotLevel2.getLevel3s().size());
//    assertEquals(level3a.getValue(), gotLevel2.getLevel3s().get(0).getValue());
//    assertEquals(level3b.getValue(), gotLevel2.getLevel3s().get(1).getValue());
  }

  private ProtobufSchema getSchema(ObjectMapper mapper, Class<?> clazz) throws Exception {
    ProtobufSchemaGenerator gen = new ProtobufSchemaGenerator();
    mapper.acceptJsonFormatVisitor(clazz, gen);
    return gen.getGeneratedSchema();
  }

  private void showBytes(byte[] bytes) {
    for (byte b : bytes) {
      System.out.print(String.format("%8s", Integer.toHexString(b)).substring(6, 8).replaceAll(" ", "0") + " ");
    }
    System.out.println();
  }

  public static class Level1 {

//    private int value;

    private Level2 level2;

//    public int getValue() {
//      return value;
//    }
//
//    public void setValue(int value) {
//      this.value = value;
//    }

    public Level2 getLevel2() {
      return level2;
    }

    public void setLevel2(Level2 level2) {
      this.level2 = level2;
    }
  }

  public static class Level2 {

    private int value;
    private List<Level3> level3s;

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public List<Level3> getLevel3s() {
      return level3s;
    }

    public void setLevel3s(List<Level3> level3s) {
      this.level3s = level3s;
    }
  }

  public static class Level3 {

    private int value;

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

  }

}
