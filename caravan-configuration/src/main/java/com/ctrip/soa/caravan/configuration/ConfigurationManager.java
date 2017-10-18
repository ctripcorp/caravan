package com.ctrip.soa.caravan.configuration;

import java.util.Collection;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ConfigurationManager {

    String getPropertyValue(String key);

    Property getProperty(String key);

    Collection<ConfigurationSource> sources();

}
