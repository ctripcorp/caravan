package com.ctrip.soa.caravan.configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ConfigurationSource {

    int priority();

    String sourceId();

    Configuration configuration();

}
