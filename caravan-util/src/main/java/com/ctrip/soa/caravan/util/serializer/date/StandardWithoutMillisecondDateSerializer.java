package com.ctrip.soa.caravan.util.serializer.date;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;

import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class StandardWithoutMillisecondDateSerializer extends AbstractDateSerializer {
    
    static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    static final String PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(((-|\\+)\\d{2}:\\d{2})|Z)?";
    static final ValueParser<TimeZone> TIME_ZONE_PARSER = new TimeZoneParser();
    
    public static final StandardWithoutMillisecondDateSerializer INSTANCE = new StandardWithoutMillisecondDateSerializer();
    
    public StandardWithoutMillisecondDateSerializer() {
        super(FORMAT, PATTERN, TIME_ZONE_PARSER);
    }
    
    static class TimeZoneParser implements ValueParser<TimeZone> {
        
        private static final String DEFAULT_TIME_ZONE_FORMAT = "((\\+|-)\\d{2}:\\d{2})|Z";
        private static final Pattern DEFAULT_TIME_ZONE_PATTERN = Pattern.compile(DEFAULT_TIME_ZONE_FORMAT);
        private static final int VALUE_LENGTH = "yyyy-MM-ddTHH:mm:ss".length();
        
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
}
