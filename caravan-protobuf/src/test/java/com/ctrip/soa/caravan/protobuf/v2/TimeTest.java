package com.ctrip.soa.caravan.protobuf.v2;

import static org.junit.Assert.assertEquals;

import com.ctrip.soa.caravan.common.value.DateValues;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.DotNetTimeRelatedProtobuf;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.TimeRelatedConverter;
import com.ctrip.soa.caravan.protobuf.v2.customization.timerelated.TimeRelatedConverter.NetTimeSpanScale;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimeTest {

  private static DatatypeFactory dataTypeFactory;

  @BeforeClass
  public static void before() throws Exception {
    dataTypeFactory = DatatypeFactory.newInstance();
  }

  @Test
  public void testReadCalendarMinDate() {
    Calendar c = TimeRelatedConverter.netDateTimeToCalendar(minPBBuf());
    assertEquals(makeCalendar(1,Calendar.JANUARY,1,0,0,0, 0).getTime().getTime(), c.getTime().getTime());
  }

  @Test
  public void testReadCalendarMaxDate() {
    Calendar c = TimeRelatedConverter.netDateTimeToCalendar(maxPBBuf());
    assertEquals(makeCalendar(9999,Calendar.DECEMBER,31,23,59,59, 999).getTime().getTime(), c.getTime().getTime());
  }

  @Test
  public void testWriteCalendarMinDate() {
    DotNetTimeRelatedProtobuf buf = TimeRelatedConverter.calendarToNetDateTime(makeCalendar(1, Calendar.JANUARY, 1, 0, 0, 0, 0));
    assertEquals(NetTimeSpanScale.MinMax, buf.getScale());
    assertEquals(-1, buf.getValue());
  }

  @Test
  public void testWriteCalendarMaxDate() {
    DotNetTimeRelatedProtobuf buf = TimeRelatedConverter.calendarToNetDateTime(makeCalendar(9999,Calendar.DECEMBER,31,23,59,59, 999));
    assertEquals(NetTimeSpanScale.MinMax, buf.getScale());
    assertEquals(1, buf.getValue());
  }



  @Test
  public void testReadXMLGregorianCalendarMinDate() {
    XMLGregorianCalendar c = TimeRelatedConverter.netDateTimeToXMLGregorianCalendar(minPBBuf());

    assertEquals(c.getYear(), 1);
    assertEquals(c.getMonth(), 1);
    assertEquals(c.getDay(), 1);
    assertEquals(c.getHour(), 0);
    assertEquals(c.getMinute(), 0);
    assertEquals(c.getSecond(), 0);
    assertEquals(c.getMillisecond(), 0);
  }

  @Test
  public void testReadXMLGregorianCalendarMaxDate() {
    XMLGregorianCalendar c = TimeRelatedConverter.netDateTimeToXMLGregorianCalendar(maxPBBuf());
    assertEquals(makeCalendar(9999,Calendar.DECEMBER,31,23,59,59, 999).getTime().getTime(), c.toGregorianCalendar().getTime().getTime());
  }

  @Test
  public void testWriteXMLGregorianCalendarMinDate() {
    DotNetTimeRelatedProtobuf buf = TimeRelatedConverter.xmlGregorianCalendarToNetDateTime(dataTypeFactory.newXMLGregorianCalendar(
        (GregorianCalendar) makeCalendar(1, Calendar.JANUARY, 1, 0, 0, 0, 0)));
    assertEquals(NetTimeSpanScale.MinMax, buf.getScale());
    assertEquals(-1, buf.getValue());
  }

  @Test
  public void testWriteXMLGregorianCalendarMaxDate() {
    DotNetTimeRelatedProtobuf buf = TimeRelatedConverter.xmlGregorianCalendarToNetDateTime(dataTypeFactory.newXMLGregorianCalendar(
        (GregorianCalendar) makeCalendar(9999,Calendar.DECEMBER,31,23,59,59, 999)));
    assertEquals(NetTimeSpanScale.MinMax, buf.getScale());
    assertEquals(1, buf.getValue());
  }

  @Test
  public void test() {
    Date date = new Date(-62135798400000L);
    GregorianCalendar c = DateValues.toGregorianCalendar(date);
    XMLGregorianCalendar xmlc = dataTypeFactory.newXMLGregorianCalendar(c);
    GregorianCalendar c2 = xmlc.toGregorianCalendar();

    System.out.println(date);
    System.out.println(c);
    System.out.println(xmlc);
    System.out.println(c2.getTime());
  }

  private Calendar makeCalendar(int year, int month, int day, int hour, int minute, int second, int milliSecode) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, minute);
    c.set(Calendar.SECOND, second);
    c.set(Calendar.MILLISECOND, milliSecode);

    return c;
  }

  private DotNetTimeRelatedProtobuf minPBBuf() {
    DotNetTimeRelatedProtobuf buf = new DotNetTimeRelatedProtobuf();
    buf.setScale(NetTimeSpanScale.MinMax);
    buf.setValue(-1);
    return buf;
  }

  private DotNetTimeRelatedProtobuf maxPBBuf() {
    DotNetTimeRelatedProtobuf buf = new DotNetTimeRelatedProtobuf();
    buf.setScale(NetTimeSpanScale.MinMax);
    buf.setValue(1);
    return buf;
  }

}
