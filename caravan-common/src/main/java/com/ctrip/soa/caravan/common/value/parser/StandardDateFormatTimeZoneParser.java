package com.ctrip.soa.caravan.common.value.parser;

import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class StandardDateFormatTimeZoneParser implements ValueParser<TimeZone> {

    private static final String DEFAULT_TIME_ZONE_FORMAT = "((\\+|-)\\d{2}:\\d{2})|Z";
    private static final Pattern DEFAULT_TIME_ZONE_PATTERN = Pattern.compile(DEFAULT_TIME_ZONE_FORMAT);
    private static final int VALUE_LENGTH = "yyyy-MM-ddTHH:mm:ss.SSS".length();

    public static final StandardDateFormatTimeZoneParser INSTANCE = new StandardDateFormatTimeZoneParser();

    private StandardDateFormatTimeZoneParser() {

    }

    @Override
    public TimeZone parse(String value) {
        if (StringValues.isNullOrWhitespace(value))
            return TimeZone.getDefault();

        if (value.length() <= VALUE_LENGTH)
            return TimeZone.getDefault();

        value = value.substring(VALUE_LENGTH);

        Matcher matcher = DEFAULT_TIME_ZONE_PATTERN.matcher(value);
        if (!matcher.matches())
            return TimeZone.getTimeZone(value);

        if (value.equals("Z") || value.equals("+00:00") || value.equals("-00:00"))
            return TimeZone.getTimeZone("UTC");

        return TimeZone.getTimeZone("GMT" + value);
    }

}
