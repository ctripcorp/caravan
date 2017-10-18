package com.ctrip.soa.caravan.util.serializer.date;

import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ctrip.soa.caravan.common.serializer.DateSerializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class WcfDateSerializer implements DateSerializer {

    private static final int ONE_MINUTE = 1000 * 60;

    public static final String PATTERN = "\\/Date\\([+-]{0,1}\\d*((-|\\+)\\d{4})*\\)\\/";

    public static final String UNSPECIFIED_OFFSET = "-0000";

    private static final Pattern _pattern = Pattern.compile(PATTERN);

    public static final WcfDateSerializer INSTANCE = new WcfDateSerializer();

    protected WcfDateSerializer() {

    }

    @Override
    public boolean isValid(String date) {
        Matcher matcher = _pattern.matcher(date);
        return matcher.matches();
    }

    @Override
    public String serialize(GregorianCalendar calendar) {
        StringBuilder sb = new StringBuilder("/Date(");
        sb.append(calendar.getTimeInMillis());
        TimeZone tz = calendar.getTimeZone();
        int offset = tz.getOffset(calendar.getTimeInMillis());
        if (offset != 0) {
            int minutes = Math.abs(offset / ONE_MINUTE);
            sb.append(offset > 0 ? "+" : "-").append(constraint(minutes / 60)).append(constraint(minutes % 60));
        }
        sb.append(")/");

        return sb.toString();
    }

    @Override
    public GregorianCalendar deserialize(String date) {
        TimeZone timeZone;
        long ts;
        String timeStr = date.substring(date.indexOf("(") + 1, date.indexOf(")"));
        int length = timeStr.length();
        if (length > 5) {
            String tz = timeStr.substring(length - 5);
            char symbol = tz.charAt(0);
            if (symbol == '+' || symbol == '-') {
                ts = Long.valueOf(timeStr.substring(0, length - 5));
                if (UNSPECIFIED_OFFSET.equals(tz))
                    timeZone = TimeZone.getDefault();
                else {
                    int hour = Integer.valueOf(tz.substring(1, 3));
                    int minute = Integer.valueOf(tz.substring(3, 5));
                    int offset = hour * 60 * 60 * 1000 + minute * 60 * 1000;
                    offset = symbol == '+' ? offset : -offset;
                    timeZone = new SimpleTimeZone(offset, "");
                }
            } else {
                ts = Long.valueOf(timeStr);
                timeZone = new SimpleTimeZone(0, "UTC");
            }
        } else {
            ts = Long.valueOf(timeStr);
            timeZone = new SimpleTimeZone(0, "UTC");
        }

        GregorianCalendar calendar = new GregorianCalendar(timeZone);
        calendar.setTimeInMillis(ts);
        return calendar;
    }

    protected static String constraint(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

}
