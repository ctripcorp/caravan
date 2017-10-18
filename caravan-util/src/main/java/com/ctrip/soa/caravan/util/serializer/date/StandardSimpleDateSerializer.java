package com.ctrip.soa.caravan.util.serializer.date;

import com.ctrip.soa.caravan.common.value.DateValues;

/**
 * Created by w.jian on 10/05/2016.
 */
public class StandardSimpleDateSerializer extends AbstractDateSerializer {
    
    public static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}{2}:\\d{2}{2}:\\d{2}{2}";
    
    public static final StandardSimpleDateSerializer INSTANCE = new StandardSimpleDateSerializer();
    
    public StandardSimpleDateSerializer() {
        super(DateValues.STANDARD_SIMPLE_DATE_FORMAT, DATE_PATTERN, null);
    }
}
