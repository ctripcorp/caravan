package com.ctrip.soa.caravan.common.metric;

import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface Metric {

    String metricId();

    Map<String, String> metadata();

}
