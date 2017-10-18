package com.ctrip.soa.caravan.util.serializer.date;

import com.ctrip.soa.caravan.common.value.DateValues;
import com.ctrip.soa.caravan.common.value.parser.StandardDateFormatTimeZoneParser;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class StandardDateSerializer extends AbstractDateSerializer {

    public static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}(((-|\\+)\\d{2}:\\d{2})|Z)";

    public static final StandardDateSerializer INSTANCE = new StandardDateSerializer();

    private StandardDateSerializer() {
        super(DateValues.STANDARD_DATE_FORMAT, DATE_PATTERN, StandardDateFormatTimeZoneParser.INSTANCE);
    }

}
