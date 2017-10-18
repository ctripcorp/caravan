package com.ctrip.soa.caravan.configuration.dynamic;

import java.util.List;

import com.ctrip.soa.caravan.configuration.ConfigurationSource;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ConfigurationSourceChangeEvent extends ChangeEvent {
    
    ConfigurationSource source();

    List<PropertyChangeEvent> propertyChangeEvents();

}
