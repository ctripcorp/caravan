package com.ctrip.soa.caravan.common.metric;

import java.util.Collection;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface EventMetric extends Metric {

    void addEvent(String eventType);

    Collection<String> getEventTypes();

    long getCount();

    long getCount(String eventType);

}
