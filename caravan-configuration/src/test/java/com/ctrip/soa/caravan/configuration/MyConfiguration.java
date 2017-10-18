package com.ctrip.soa.caravan.configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MyConfiguration implements Configuration {

    private String _value;

    public MyConfiguration(final String value) {
        _value = value;
    }

    @Override
    public String getPropertyValue(String key) {
        return _value;
    }
}