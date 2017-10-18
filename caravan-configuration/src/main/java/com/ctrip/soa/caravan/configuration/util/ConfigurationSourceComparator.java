package com.ctrip.soa.caravan.configuration.util;

import java.util.Comparator;

import com.ctrip.soa.caravan.configuration.ConfigurationSource;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ConfigurationSourceComparator implements Comparator<ConfigurationSource> {

    public static final ConfigurationSourceComparator DEFAULT = new ConfigurationSourceComparator();

    @Override
    public int compare(ConfigurationSource o1, ConfigurationSource o2) {
        if (o1 == o2)
            return 0;

        if (o1 == null)
            return 1;

        if (o2 == null)
            return -1;

        if (o1.priority() == o2.priority())
            return 0;

        return o1.priority() < o2.priority() ? 1 : -1;
    }

}
