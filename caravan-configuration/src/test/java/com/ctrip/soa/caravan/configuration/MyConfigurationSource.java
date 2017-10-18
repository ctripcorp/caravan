package com.ctrip.soa.caravan.configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MyConfigurationSource implements ConfigurationSource {

    private int _priority = 0;

    public MyConfigurationSource(final int priority) {
        _priority = priority;
    }

    @Override
    public int priority() {
        return _priority;
    }

    @Override
    public Configuration configuration() {
        return new MyConfiguration("my: " + _priority);
    }

    @Override
    public String sourceId() {
        return null;
    }
}