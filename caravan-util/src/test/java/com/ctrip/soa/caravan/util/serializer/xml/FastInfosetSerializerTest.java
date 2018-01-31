package com.ctrip.soa.caravan.util.serializer.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.Test;

public class FastInfosetSerializerTest {

  @Test
  public void testSerializeAndDeserializeEqual() {
    XMLPojo p = new XMLPojo();
    p.setName(makeString(1000));
    p.setId(123456789L);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    FastInfosetSerializer.INSTANCE.serialize(bout, p);

    XMLPojo got = FastInfosetSerializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), XMLPojo.class);

    assertEquals(p, got);
  }

  @Test
  public void testSmallerThanPlainXML() {
    XMLPojo p = new XMLPojo();
    p.setName(makeString(1000));
    p.setId(123456789L);

    ByteArrayOutputStream fastInfosetBout = new ByteArrayOutputStream();
    FastInfosetSerializer.INSTANCE.serialize(fastInfosetBout, p);

    ByteArrayOutputStream plainXMLBout = new ByteArrayOutputStream();
    JAXBXmlSerializer.INSTANCE.serialize(plainXMLBout, p);

    assertTrue(fastInfosetBout.toByteArray().length < plainXMLBout.toByteArray().length);
  }

  private String makeString(int len) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append("A");
    }
    return sb.toString();
  }

}