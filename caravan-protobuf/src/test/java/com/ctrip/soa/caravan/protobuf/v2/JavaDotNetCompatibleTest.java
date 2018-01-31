package com.ctrip.soa.caravan.protobuf.v2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.ctrip.soa.caravan.common.serializer.SerializationException;
import com.ctrip.soa.caravan.common.value.DateValues;
import com.ctrip.soa.caravan.protobuf.v2.customization.CustomProtobufParser;
import com.ctrip.soa.caravan.protobuf.v2.Pojo14.SomeEnum;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.TimeRelatedConverter;
import com.ctrip.soa.framework.soa.testservice.v1.ArrayTypes;
import com.ctrip.soa.framework.soa.testservice.v1.BasicTypes1;
import com.ctrip.soa.framework.soa.testservice.v1.BasicTypes2;
import com.ctrip.soa.framework.soa.testservice.v1.ComplexType;
import com.ctrip.soa.framework.soa.testservice.v1.EmbedType1;
import com.ctrip.soa.framework.soa.testservice.v1.EnumType1;
import com.ctrip.soa.framework.soa.testservice.v1.EnumType2;
import com.ctrip.soa.framework.soa.testservice.v1.Level1;
import com.ctrip.soa.framework.soa.testservice.v1.Level2;
import com.ctrip.soa.framework.soa.testservice.v1.Level3;
import com.ctrip.soa.framework.soa.testservice.v1.ListTypes;
import com.ctrip.soa.framework.soa.testservice.v1.TypeWithEnum;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;

/**
 * Created by marsqing on 17/03/2017.
 */
public class JavaDotNetCompatibleTest {

  // http://tools.soa.fws.qa.nt.ctripcorp.com/test-service/_types?data=basic1      &format=x-protobuf

  @Test
  public void testBasicType1() throws Exception {
    BasicTypes1 p = makeBasicType1();

    showSchema(BasicTypes1.class);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    BasicTypes1 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), BasicTypes1.class);

    assertBasicType1Equal(p, got);
  }

  private void assertBasicType1Equal(BasicTypes1 p, BasicTypes1 got) {
    assertEquals(p.getByteType(), got.getByteType());
    assertEquals(p.getShortType(), got.getShortType());
    assertEquals(p.getIntType(), got.getIntType());
    assertEquals(p.getLongType(), got.getLongType());
    assertEquals(p.getFloatType(), got.getFloatType(), 0.01);
    assertEquals(p.getDoubleType(), got.getDoubleType(), 0.01);
    assertEquals(p.getDecimalType(), got.getDecimalType());
    assertEquals(p.isBooleanType(), got.isBooleanType());
    assertEquals(p.getStringType(), got.getStringType());
    assertEquals(p.getDateTimeType(), got.getDateTimeType());
    assertArrayEquals(p.getBinaryType(), got.getBinaryType());
    assertEquals(p.getCalendar(), got.getCalendar());
  }

  private BasicTypes1 makeBasicType1() {
    BasicTypes1 p = new BasicTypes1();
    p.setByteType((byte) 2);
    p.setShortType((short) 3);
    p.setIntType(4);
    p.setLongType(5);
    p.setFloatType(100000.99f);
    p.setDoubleType(19.99);
    //19223372036854775807
    p.setDecimalType(new BigDecimal("1" + Long.toString(Long.MAX_VALUE)));
    p.setBooleanType(true);
    p.setStringType("aaaaa");
    p.setDateTimeType(toGregorianCalendar(1489745720555L));
    p.setBinaryType(new byte[]{1, 2, 3, 4, 5});
    p.setCalendar(toCalendar(1489745720555L));

    return p;
  }

  private Calendar toCalendar(long millis) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(millis);
    return c;
  }

  @Test
  public void testBasicType2() throws Exception {
    BasicTypes2 p = makeBasicType2();

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    BasicTypes2 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), BasicTypes2.class);

    assertBasicType2Equal(p, got);
  }

  private void assertBasicType2Equal(BasicTypes2 p, BasicTypes2 got) {
    assertEquals(p.getUbyteType(), got.getUbyteType());
    assertEquals(p.getUintType(), got.getUintType());
    assertEquals(p.getUshortType(), got.getUshortType());
    assertEquals(p.getUlongType(), got.getUlongType());
  }

  private BasicTypes2 makeBasicType2() {
    BasicTypes2 p = new BasicTypes2();
    p.setUbyteType((short) 255); // max byte value of csharp
    p.setUintType(4294967295L); // max uint value of csharp
    p.setUshortType(65535); // max ushort value of csharp
    p.setUlongType(new BigInteger("17446744073709551615"));
    return p;
  }

  @Test
  public void testArrayTypes() {
    showSchema(ArrayTypes.class);

    ArrayTypes p = makeArrayTypes();

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    ArrayTypes got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), ArrayTypes.class);

    assertArrayTypesEqual(p, got);
  }

  private void assertArrayTypesEqual(ArrayTypes p, ArrayTypes got) {
    assertEquals(p.getByteType(), got.getByteType());
    assertEquals(p.getShortType(), got.getShortType());
    assertEquals(p.getIntType(), got.getIntType());
    assertEquals(p.getLongType(), got.getLongType());
    assertFloatListEqual(p.getFloatType(), got.getFloatType(), 0.1F);
    assertDoubleListEqual(p.getDoubleType(), got.getDoubleType(), 0.1D);
    assertBigDecimalListEqual(p.getDecimalType(), got.getDecimalType(), 0.1D);
    assertEquals(p.getBooleanType(), got.getBooleanType());
    assertEquals(p.getDateTimeType(), got.getDateTimeType());
    assertEquals(p.getStringType(), got.getStringType());
    assertEquals(p.getUbyteType(), got.getUbyteType());
    assertEquals(p.getUintType(), got.getUintType());
    assertEquals(p.getUshortType(), got.getUshortType());
    assertEquals(p.getUlongType(), got.getUlongType());
  }

  private void assertListTypesEqual(ListTypes p, ListTypes got) {
    assertEquals(p.getByteType(), got.getByteType());
    assertEquals(p.getShortType(), got.getShortType());
    assertEquals(p.getIntType(), got.getIntType());
    assertEquals(p.getLongType(), got.getLongType());
    assertFloatListEqual(p.getFloatType(), got.getFloatType(), 0.1F);
    assertDoubleListEqual(p.getDoubleType(), got.getDoubleType(), 0.1D);
    assertBigDecimalListEqual(p.getDecimalType(), got.getDecimalType(), 0.1D);
    assertEquals(p.getBooleanType(), got.getBooleanType());
    assertEquals(p.getDateTimeType(), got.getDateTimeType());
    assertEquals(p.getStringType(), got.getStringType());
    assertEquals(p.getUintType(), got.getUintType());
    assertEquals(p.getUshortType(), got.getUshortType());
    assertEquals(p.getUlongType(), got.getUlongType());
    assertDurationListEqual(p.getDurationType(), got.getDurationType());
  }

  private ArrayTypes makeArrayTypes() {
    ArrayTypes p = new ArrayTypes();
    List<Byte> bytes = Arrays.asList((byte) 1, (byte) 2);
    List<Short> shorts = Arrays.asList((short) 1, (short) 2);
    List<Integer> integers = Arrays.asList(1, 2);
    List<Long> longs = Arrays.asList(1L, 2L);
    List<Float> floats = Arrays.asList(1.1f, 2.2f);
    List<Double> doubles = Arrays.asList(1.1D, 2.2D);
    List<BigDecimal> bigDecimals = Arrays.asList(new BigDecimal(16), new BigDecimal(16));
    List<Boolean> booleans = Arrays.asList(Boolean.TRUE, Boolean.FALSE);
    List<XMLGregorianCalendar> gregorianCalendars = Arrays.asList(toGregorianCalendar(1489745720555L - 3600000), toGregorianCalendar(1489745720555L));
    List<String> strings = Arrays.asList("aa", "bbbb");
    List<Short> ubytes = Arrays.asList((short) 1, (short) 2);
    List<Long> uintegers = Arrays.asList(1L, 2L);
    List<Integer> ushorts = Arrays.asList(1, 2);
    List<BigInteger> ulongs = Arrays.asList(new BigInteger("1"), new BigInteger("2"));

    p.setByteType(bytes);

    p.setShortType(shorts);
    p.setIntType(integers);
    p.setLongType(longs);
    p.setFloatType(floats);
    p.setDoubleType(doubles);
    p.setDecimalType(bigDecimals);
    p.setBooleanType(booleans);
    p.setDateTimeType(gregorianCalendars);
    p.setStringType(strings);
    p.setUbyteType(ubytes);
    p.setUintType(uintegers);
    p.setUshortType(ushorts);
    p.setUlongType(ulongs);

    return p;
  }

  private ListTypes makeListTypes() {
    ListTypes p = new ListTypes();
    List<Byte> bytes = Arrays.asList((byte) 1, (byte) 2);
    List<Short> shorts = Arrays.asList((short) 1, (short) 2);
    List<Integer> integers = Arrays.asList(1, 2);
    List<Long> longs = Arrays.asList(1L, 2L);
    List<Float> floats = Arrays.asList(1.1f, 2.2f);
    List<Double> doubles = Arrays.asList(1.1D, 2.2D);
    List<BigDecimal> bigDecimals = Arrays.asList(new BigDecimal(16), new BigDecimal(16));
    List<Boolean> booleans = Arrays.asList(Boolean.TRUE, Boolean.FALSE);
    List<XMLGregorianCalendar> gregorianCalendars = Arrays.asList(toGregorianCalendar(1489745720555L - 3600000), toGregorianCalendar(1489745720555L));
    List<String> strings = Arrays.asList("aa", "bbbb");
    List<Long> uintegers = Arrays.asList(1L, 2L);
    List<Integer> ushorts = Arrays.asList(1, 2);
    List<BigInteger> ulongs = Arrays.asList(new BigInteger("1"), new BigInteger("2"));
    List<Duration> durations = Arrays.asList(TimeRelatedConverter.dataTypeFactory.newDuration(1L), TimeRelatedConverter.dataTypeFactory.newDuration(1L));

    p.setByteType(bytes);
    p.setShortType(shorts);
    p.setIntType(integers);
    p.setLongType(longs);
    p.setFloatType(floats);
    p.setDoubleType(doubles);
    p.setDecimalType(bigDecimals);
    p.setBooleanType(booleans);
    p.setDateTimeType(gregorianCalendars);
    p.setStringType(strings);
    p.setUintType(uintegers);
    p.setUshortType(ushorts);
    p.setUlongType(ulongs);
    p.setDurationType(durations);

    return p;
  }

  @Test
  public void testEnum() {
    showSchema(TypeWithEnum.class);

    EmbedType1 embed = new EmbedType1();
    embed.setType1(EnumType1.MEDIUM);

    TypeWithEnum p = new TypeWithEnum();
    p.setType1(EnumType1.LARGE);
    p.setType2(EnumType2.OK);
    p.setEmbed(embed);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    TypeWithEnum got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), TypeWithEnum.class);
    assertEquals(p.getType1(), got.getType1());
    assertEquals(p.getType2(), got.getType2());
    assertEquals(p.getEmbed().getType1(), got.getEmbed().getType1());
  }

  @Test
  public void testNotSoComplextType() {
    showSchema(Level1.class);

    Level1 level1 = new Level1();
    Level2 level2 = new Level2();
    Level3 level3 = new Level3();

//    s.setDecimalType(new BigDecimal(1.1));

    level1.setValue(1);
    level2.setValue(2);
    level3.setValue(3);
    level1.setLevel2(level2);
    level2.setLevel3(level3);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, level1);

    showBytes(bout.toByteArray());

    Level1 gotLevel1 = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Level1.class);

//    byte[] bytes = new byte[]{0x08, 0x01, 0x12, 0x06, 0x08, 0x02, 0x12, 0x02, 0x08, 0x03};
//    Level1 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bytes), Level1.class);

    assertEquals(level1.getValue(), gotLevel1.getValue());
    assertEquals(level2.getValue(), gotLevel1.getLevel2().getValue());
    assertEquals(level3.getValue(), gotLevel1.getLevel2().getLevel3().getValue());
//    assertTrue(ps.getDecimalType().subtract(gots.getDecimalType()).doubleValue() < 0.1D);
  }

  @Test
  public void testComplextType() {
    showSchema(ComplexType.class);

    ComplexType p = new ComplexType();

    p.setIntType(1);
    p.setBasicTypes1(makeBasicType1());
    p.setBasicTypes2(makeBasicType2());
    p.setEnumType1(EnumType1.MEDIUM);
    p.setEnumType2(EnumType2.OK);
    p.setArrayTypes(makeArrayTypes());
    p.setStringType("test");
    p.setListTypes(makeListTypes());

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
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    ComplexType got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), ComplexType.class);

    assertEquals(p.getIntType(), got.getIntType());
    assertBasicType1Equal(p.getBasicTypes1(), got.getBasicTypes1());
    assertBasicType2Equal(p.getBasicTypes2(), got.getBasicTypes2());
    assertEquals(p.getEnumType1(), got.getEnumType1());
    assertEquals(p.getEnumType2(), got.getEnumType2());
    assertArrayTypesEqual(p.getArrayTypes(), got.getArrayTypes());
    assertListTypesEqual(p.getListTypes(), got.getListTypes());
    assertEquals(p.getStringType(), got.getStringType());
  }

  private XMLGregorianCalendar toGregorianCalendar(long millis) {
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(DateValues.toGregorianCalendar(new Date(millis)));
    } catch (DatatypeConfigurationException e) {
      throw new RuntimeException("", e);
    }
  }

  private void assertBigDecimalListEqual(List<BigDecimal> exp, List<BigDecimal> actual, double delta) {
    for (int i = 0; i < exp.size(); i++) {
      assertTrue(exp.get(i).subtract(actual.get(i)).doubleValue() < delta);
    }
  }

  private void assertDurationListEqual(List<Duration> exp, List<Duration> actual) {
    Date start = new Date(0L);
    for (int i = 0; i < exp.size(); i++) {
      assertEquals(exp.get(i).getTimeInMillis(start), actual.get(i).getTimeInMillis(start));
    }
  }

  private void assertFloatListEqual(List<Float> exp, List<Float> actual, float delta) {
    assertArrayEquals(toFloatArray(exp), toFloatArray(actual), delta);
  }

  private void assertDoubleListEqual(List<Double> exp, List<Double> actual, double delta) {
    assertArrayEquals(toDoubleArray(exp), toDoubleArray(actual), delta);
  }

  private float[] toFloatArray(List<Float> floats) {
    float[] floatArray = new float[floats.size()];

    int i = 0;
    for (Float f : floats) {
      floatArray[i++] = f;
    }

    return floatArray;
  }

  private double[] toDoubleArray(List<Double> doubles) {
    double[] doubleArray = new double[doubles.size()];

    int i = 0;
    for (Double d : doubles) {
      doubleArray[i++] = d;
    }

    return doubleArray;
  }

  @Test
  public void showMillis() {
    Calendar c = Calendar.getInstance();
    c.set(2017, Calendar.MARCH, 17, 18, 15, 20);
    c.set(Calendar.MILLISECOND, 555);

    System.out.println(c.getTime().getTime());  //1489745720555
  }

  @Test
  public void testBigInteger() {
    BigInteger bi = new BigInteger(Long.toString(Long.MAX_VALUE));
    bi = bi.add(new BigInteger("1"));

    assertEquals("8000000000000000", Long.toHexString(bi.longValue()));
  }

  @Test
  public void testCalendar() {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    Calendar p = toCalendar(1489745720555L);
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    Calendar got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Calendar.class);
    assertEquals(p, got);
  }

  @Test
  public void testArrayType() {
    Pojo4 p = new Pojo4();
    p.setBooleans(new boolean[]{true, false});
    p.setBytes(new byte[]{1, 2});
    p.setChars(new char[]{'a', 'b'});
    p.setDoubles(new double[]{1.1d, 2.2d});
    p.setFloats(new float[]{1.1f, 2.2f});
    p.setInts(new int[]{1, 2});
    p.setLongs(new long[]{1L, 2L});
    p.setShorts(new short[]{1, 2});

    p.setWbooleans(new Boolean[]{true, false});
    p.setWbytes(new Byte[]{1, 2});
    p.setWchars(new Character[]{'a', 'b'});
    p.setWdoubles(new Double[]{1.1d, 2.2d});
    p.setWfloats(new Float[]{1.1f, 2.2f});
    p.setWints(new Integer[]{1, 2});
    p.setWlongs(new Long[]{1L, 2L});
    p.setWshorts(new Short[]{1, 2});

    p.setCalendars(new Calendar[]{toCalendar(1489745720000L), toCalendar(1489745720555L)});

    showSchema(Pojo4.class);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    Pojo4 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo4.class);

    assertArrayEquals(p.getBooleans(), got.getBooleans());
    assertArrayEquals(p.getBytes(), got.getBytes());
    assertArrayEquals(p.getChars(), got.getChars());
    assertArrayEquals(p.getDoubles(), got.getDoubles(), 0.1d);
    assertArrayEquals(p.getFloats(), got.getFloats(), 0.1f);
    assertArrayEquals(p.getInts(), got.getInts());
    assertArrayEquals(p.getLongs(), got.getLongs());
    assertArrayEquals(p.getShorts(), got.getShorts());
    assertArrayEquals(p.getWbooleans(), got.getWbooleans());
    assertArrayEquals(p.getWbytes(), got.getWbytes());
    assertArrayEquals(p.getWchars(), got.getWchars());
    assertDoubleListEqual(Arrays.asList(p.getWdoubles()), Arrays.asList(got.getWdoubles()), 0.1d);
    assertFloatListEqual(Arrays.asList(p.getWfloats()), Arrays.asList(got.getWfloats()), 0.1f);
    assertArrayEquals(p.getWints(), got.getWints());
    assertArrayEquals(p.getWlongs(), got.getWlongs());
    assertArrayEquals(p.getWshorts(), got.getWshorts());

    assertArrayEquals(p.getCalendars(), got.getCalendars());
  }

  @Test
  public void testEmptyArray() {
    Pojo4 p = new Pojo4();
    p.setBooleans(new boolean[0]);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    Pojo4 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo4.class);

    assertNull(got.getBooleans());
  }

  @Test
  public void testStringKeyMapType() {
    for (int i = 0; i < 2; i++) {
      Map<String, Embed> map = new HashMap<>();
      map.put("a", new Embed(1));
      map.put("b", new Embed(2));

      Pojo5 p = new Pojo5();
      p.setMap(map);

      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

      showBytes(bout.toByteArray());

      Pojo5 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo5.class);
      assertEquals(p.getMap(), got.getMap());
    }

    for (int i = 0; i < 2; i++) {
      Map<String, BasicTypes1> map2 = new HashMap<>();
      map2.put("a", makeBasicType1());
      map2.put("b", makeBasicType1());

      Pojo7 p2 = new Pojo7();
      p2.setMap(map2);

      ByteArrayOutputStream bout2 = new ByteArrayOutputStream();
      JacksonProtobuf2Serializer.INSTANCE.serialize(bout2, p2);

      showBytes(bout2.toByteArray());

      Pojo7 got2 = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout2.toByteArray()), Pojo7.class);

      assertEquals(p2.getMap().size(), got2.getMap().size());
      assertBasicType1Equal(p2.getMap().get("a"), got2.getMap().get("a"));
      assertBasicType1Equal(p2.getMap().get("b"), got2.getMap().get("b"));
    }
  }

  @Test
  public void testNonStringKeyMapType() {
    Map<Embed, BasicTypes1> map = new HashMap<>();
    map.put(new Embed(1), makeBasicType1());
    map.put(new Embed(2), null);
    map.put(new Embed(3), makeBasicType1());

    Pojo8 p = new Pojo8();
    p.setMap(map);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    Pojo8 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo8.class);

    assertEquals(p.getMap().size(), got.getMap().size());
    assertBasicType1Equal(p.getMap().get(new Embed(1)), got.getMap().get(new Embed(1)));
    assertNull(got.getMap().get(new Embed(2)));
    assertBasicType1Equal(p.getMap().get(new Embed(3)), got.getMap().get(new Embed(3)));
  }

  @Test
  public void testMapWithSomeValueNull() {
    Map<String, Embed> map = new HashMap<>();
    map.put("a", null);
    map.put("b", new Embed(1));

    Pojo5 p = new Pojo5();
    p.setMap(map);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    Pojo5 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo5.class);
    assertEquals(p.getMap(), got.getMap());
  }

  @Test
  public void testMapWithValuesAllNull() {
    Map<String, Embed> map = new HashMap<>();
    map.put("a", null);
    map.put("b", null);

    Pojo5 p = new Pojo5();
    p.setMap(map);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try {
      JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);
      fail();
    } catch (Exception e) {
    }

  }

  @Test
  public void testFloat() {
    Pojo9 p = new Pojo9();
    p.setFloatValue(123.456f);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    Pojo9 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo9.class);
    assertEquals(p.getFloatValue(), got.getFloatValue(), 0.1f);
  }

  @Test
  public void testBigDecimalNormal() {
    String[] values = new String[]{"20.5", "-20.5", "2", "-2", "0", "20", "20000", "-20000", "0.000001", "-0.000001"};

    for (String value : values) {
        assertTrue(testBigDecimalWork(value));
    }
  }

  @Test
  public void testBigDecimalLongMinValue() {
      assertTrue(testBigDecimalWork(Long.toString(Long.MIN_VALUE)));
  }

  @Test
  public void testBigDecimalLongMaxValue() {
      assertTrue(testBigDecimalWork(Long.toString(Long.MAX_VALUE)));
  }

  @Test(expected = SerializationException.class)
  public void testBigDecimalDoubleMinValue() {
      testBigDecimalWork(Double.toString(Double.MIN_VALUE));
  }

  @Test(expected = SerializationException.class)
  public void testBigDecimalDoubleMaxValue() {
      testBigDecimalWork(Double.toString(Double.MAX_VALUE));
  }

  @Test(expected = SerializationException.class)
  public void testBigDecimalFloatMinValue() {
      testBigDecimalWork(Float.toString(Float.MIN_VALUE));
  }

  @Test(expected = SerializationException.class)
  public void testBigDecimalFloatMaxValue() {
      testBigDecimalWork(Float.toString(Float.MAX_VALUE));
  }

  private boolean testBigDecimalWork(String value) {
      Pojo10 p = new Pojo10();
      p.setBigDecimal(new BigDecimal(value));

      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

      Pojo10 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo10.class);
      return p.getBigDecimal().equals(got.getBigDecimal());
  }

  @Test
  public void testDotNetNegativeInt() {
    byte[] buf = new byte[]{0x08, (byte) 0xe5, (byte) 0xf6, (byte) 0xfc, (byte) 0xfe, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01};

    Pojo11 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(buf), Pojo11.class);
    assertEquals(-2147483L, got.getValue());
  }

  @Test
  public void testDotNetNegativeIntSlowMode() {
    CustomProtobufParser.slowMode = true;

    byte[] buf = new byte[]{0x08, (byte) 0xe5, (byte) 0xf6, (byte) 0xfc, (byte) 0xfe, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01};

    Pojo11 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(buf), Pojo11.class);
    assertEquals(-2147483L, got.getValue());
  }

  @Test
  public void testDotNetPositiveIntSlowMode() {
    CustomProtobufParser.slowMode = true;

    byte[] buf = new byte[]{0x08, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x07};

    Pojo13 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(buf), Pojo13.class);
    assertEquals(Integer.MAX_VALUE, got.getValue());
  }

  @Test
  public void testDotNetZeroIntSlowMode() {
    CustomProtobufParser.slowMode = true;

    byte[] buf = new byte[]{0x08, (byte) 0x00};

    Pojo13 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(buf), Pojo13.class);
    assertEquals(0, got.getValue());
  }

  @Test
  public void testNegativeByte() {
    Pojo12 p = new Pojo12();
    p.setValue((byte) -127);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    Pojo12 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo12.class);
    assertEquals(-127, got.getValue());
  }

  @Test
  public void testCollectionOfEnum() {
    Pojo14 p = new Pojo14();
    p.setList(Arrays.asList(SomeEnum.Enum3, SomeEnum.Enum2, SomeEnum.Enum1));
    p.setArray(new SomeEnum[]{SomeEnum.Enum2, SomeEnum.Enum1, SomeEnum.Enum3});

    Map<SomeEnum, String> keyOfMap = new HashMap<>();
    keyOfMap.put(SomeEnum.Enum1, "a");
    keyOfMap.put(SomeEnum.Enum2, "b");
    keyOfMap.put(SomeEnum.Enum3, "c");
    p.setKeyOfMap(keyOfMap);

    Map<String, SomeEnum> valueOfMap = new HashMap<>();
    valueOfMap.put("a", SomeEnum.Enum1);
    valueOfMap.put("b", SomeEnum.Enum3);
    valueOfMap.put("c", SomeEnum.Enum2);
    p.setValueOfMap(valueOfMap);

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    JacksonProtobuf2Serializer.INSTANCE.serialize(bout, p);

    showBytes(bout.toByteArray());

    Pojo14 got = JacksonProtobuf2Serializer.INSTANCE.deserialize(new ByteArrayInputStream(bout.toByteArray()), Pojo14.class);
    assertEquals(p.getList(), got.getList());
    assertArrayEquals(p.getArray(), got.getArray());
    assertEquals(p.getKeyOfMap(), got.getKeyOfMap());
    assertEquals(p.getValueOfMap(), got.getValueOfMap());
  }

  private void showBytes(byte[] bytes) {
    for (byte b : bytes) {
      System.out.print(String.format("%8s", Integer.toHexString(b)).substring(6, 8).replaceAll(" ", "0") + " ");
    }
    System.out.println();
  }

  private void showSchema(Class<?> clazz) {
    System.out.println(new ProtobufMapperWrapper().getSchema(clazz).getSource());
  }

}
