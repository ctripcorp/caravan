package com.ctrip.soa.caravan.util.serializer.date;

import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.ctrip.soa.caravan.common.serializer.DateSerializer;
import com.ctrip.soa.caravan.common.value.DateValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class AbstractDateSerializer implements DateSerializer {

    private String _dateFormat;
    private ValueParser<TimeZone> _timeZoneParser;
    private Pattern _pattern;

    public AbstractDateSerializer(String dateFormat, String datePattern, ValueParser<TimeZone> timeZoneParser) {
        StringArgumentChecker.DEFAULT.check(dateFormat, "dateFormat");

        _dateFormat = dateFormat;
        _pattern = StringValues.isNullOrWhitespace(datePattern) ? null : Pattern.compile(datePattern);
        _timeZoneParser = timeZoneParser;
    }

    @Override
    public boolean isValid(String date) {
        if (_pattern == null)
            return true;

        return _pattern.matcher(date).matches();
    }

    @Override
    public String serialize(GregorianCalendar calendar) {
        return DateValues.toString(calendar, _dateFormat);
    }

    @Override
    public GregorianCalendar deserialize(String date) {
        return DateValues.parseCalendar(date, _dateFormat, _timeZoneParser);
    }

}
