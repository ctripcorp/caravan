package com.ctrip.soa.caravan.protobuf.v2.customization.timerelated;

import com.ctrip.soa.caravan.common.value.DateValues;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by marsqing on 24/03/2017.
 */
public class TimeRelatedConverter {

  public final static DatatypeFactory dataTypeFactory;

  private final static long EIGHT_HOUR_MILLIS = 8L * 3600 * 1000;

  // milliseconds between 9999-12-31 23:59:59.999 and 1970-0-0 00:00:00.000
  public final static long DOT_NET_DATETIME_MAX_MILLIS = 253402271999999L;

  // milliseconds between 1970-0-0 00:00:00.000 and 0000-0-0 00:00:00.000
  public final static long DOT_NET_DATETIME_MIN_MILLIS = -62135798400000L;

  public final static long DOT_NET_TIMESPAN_MAX_MILLIS = 922337203685477L;

  public final static long DOT_NET_TIMESPAN_MIN_MILLIS = -922337203685477L;

  public final static GregorianCalendar EPOCH = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

  static {
    try {
      dataTypeFactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new RuntimeException("Unexpected exception", e);
    }

    EPOCH.setTimeInMillis(0);
  }

  public static DotNetTimeRelatedProtobuf durationToNetDateTime(Duration duration) {
    long millis = duration.getTimeInMillis(EPOCH);

    if(millis >= DOT_NET_TIMESPAN_MAX_MILLIS) {
      return new DotNetTimeRelatedProtobuf(1L, NetTimeSpanScale.MinMax);
    }

    // FIXME
    // dataTypeFactory.newDuration(DOT_NET_TIMESPAN_MIN_MILLIS).getTimeInMillis(EPOCH)
    // ==
    // DOT_NET_TIMESPAN_MIN_MILLIS - 17971200000L
    if(millis <= DOT_NET_TIMESPAN_MIN_MILLIS - 17971200000L) {
      return new DotNetTimeRelatedProtobuf(-1L, NetTimeSpanScale.MinMax);
    }

    // DatatypeFactory.newDuration() based on GMT
    return new DotNetTimeRelatedProtobuf(millis, NetTimeSpanScale.Milliseconds);
  }

  public static Duration netDateTimeToDuration(DotNetTimeRelatedProtobuf dateTime) {
    return dataTypeFactory.newDuration(netTimeSpanToMillis(dateTime));
  }

  public static DotNetTimeRelatedProtobuf xmlGregorianCalendarToNetDateTime(XMLGregorianCalendar calendar) {
    return calendarToNetDateTime(calendar.toGregorianCalendar());
  }

  public static XMLGregorianCalendar netDateTimeToXMLGregorianCalendar(DotNetTimeRelatedProtobuf dateTime) {
    return dataTypeFactory.newXMLGregorianCalendar(DateValues.toGregorianCalendar(new Date(netDateTimeToMillis(dateTime, -EIGHT_HOUR_MILLIS))));
  }

  public static DotNetTimeRelatedProtobuf calendarToNetDateTime(Calendar c) {
    long millis = c.getTimeInMillis();

    if (millis >= DOT_NET_DATETIME_MAX_MILLIS) {
      return new DotNetTimeRelatedProtobuf(1L, NetTimeSpanScale.MinMax);
    }

    // FIXME inconsistent convertion between GregorianCalendar and XMLGregorianCalendar for DOT_NET_DATETIME_MIN_MILLIS
    /*
    Date date = new Date(DOT_NET_DATETIME_MIN_MILLIS);
    GregorianCalendar c = DateValues.toGregorianCalendar(date);
    XMLGregorianCalendar xmlc = dataTypeFactory.newXMLGregorianCalendar(c);
    GregorianCalendar c2 = xmlc.toGregorianCalendar();
    c2.getTime().getTime() != c.getTime().getTime()
    c2.getTime().getTime() - c.getTime().getTime() = 48L * 3600000
    */
    if(millis <= DOT_NET_DATETIME_MIN_MILLIS + 48L * 3600000) {
      return new DotNetTimeRelatedProtobuf(-1L, NetTimeSpanScale.MinMax);
    }

    return new DotNetTimeRelatedProtobuf(c.getTimeInMillis() + EIGHT_HOUR_MILLIS, NetTimeSpanScale.Milliseconds);

  }

  public static Calendar netDateTimeToCalendar(DotNetTimeRelatedProtobuf dateTime) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(netDateTimeToMillis(dateTime, -EIGHT_HOUR_MILLIS));
    return c;
  }

  public static long netTimeSpanToMillis(DotNetTimeRelatedProtobuf dateTime) {
    long ticks = readTicks(dateTime);

    long millis;
    if (ticks == Long.MAX_VALUE) {
      millis = DOT_NET_TIMESPAN_MAX_MILLIS;
    } else if (ticks == Long.MIN_VALUE) {
      millis = DOT_NET_TIMESPAN_MIN_MILLIS;
    } else {
      millis = ticks / TicksPerMillisecond;
    }

    return millis;

  }

  public static long netDateTimeToMillis(DotNetTimeRelatedProtobuf dateTime, long delta) {
    long ticks = readTicks(dateTime);

    long millis;
    if (ticks == Long.MAX_VALUE) {
      millis = DOT_NET_DATETIME_MAX_MILLIS;
    } else if (ticks == Long.MIN_VALUE) {
      millis = DOT_NET_DATETIME_MIN_MILLIS;
    } else {
      millis = ticks / TicksPerMillisecond;
      millis += delta;
    }

    return millis;

  }

  public static long readTicks(DotNetTimeRelatedProtobuf dateTime) {
    long value = dateTime.getValue();

    switch (dateTime.getScale()) {
      case NetTimeSpanScale.Days:
        return value * TicksPerDay;
      case NetTimeSpanScale.Hours:
        return value * TicksPerHour;
      case NetTimeSpanScale.Minutes:
        return value * TicksPerMinute;
      case NetTimeSpanScale.Seconds:
        return value * TicksPerSecond;
      case NetTimeSpanScale.Milliseconds:
        return value * TicksPerMillisecond;
      case NetTimeSpanScale.Ticks:
        return value;
      case NetTimeSpanScale.MinMax:
        return value == 1L ? Long.MAX_VALUE : Long.MIN_VALUE;
      default:
        throw new RuntimeException("Unknown scale: " + dateTime.getScale());
    }
  }

  public final static long TicksPerDay = 864000000000L;
  public final static long TicksPerHour = 36000000000L;
  public final static long TicksPerMillisecond = 10000L;
  public final static long TicksPerMinute = 600000000L;
  public final static long TicksPerSecond = 10000000L;

  public static class NetTimeSpanScale {

    public final static int Days = 0;
    public final static int Hours = 1;
    public final static int Minutes = 2;
    public final static int Seconds = 3;
    public final static int Milliseconds = 4;
    public final static int Ticks = 5;
    public final static int MinMax = 15;
  }

}
