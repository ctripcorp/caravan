package com.ctrip.soa.caravan.configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MyProperty implements Property {

    String _k = null;

    String _v = null;

    public MyProperty(String k, String v) {
        _k = k;
        _v = v;
    }

    @Override
    public String key() {
        return _k;
    }

    @Override
    public String value() {
        return _v;
    }
}