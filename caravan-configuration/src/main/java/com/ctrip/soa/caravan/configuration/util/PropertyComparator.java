package com.ctrip.soa.caravan.configuration.util;

import java.util.Comparator;

import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.configuration.Property;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PropertyComparator implements Comparator<Property> {

    public static final PropertyComparator DEFAULT = new PropertyComparator();

    @Override
    public int compare(Property o1, Property o2) {
        if (o1 == o2)
            return 0;

        if (StringValues.isNullOrWhitespace(o1.key()))
            return -1;

        if (StringValues.isNullOrWhitespace(o2.key()))
            return 1;

        return o1.key().compareTo(o2.key());
    }

}
