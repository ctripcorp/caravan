package com.ctrip.soa.caravan.configuration;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class NullProperty implements Property {

    public static final String NULL_KEY = "null_property_key";

    private String _key;

    public static final NullProperty INSTANCE = new NullProperty();

    private NullProperty() {
        _key = NULL_KEY;
    }

    @Override
    public String key() {
        return _key;
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public String toString() {
        return "key: null, value: null";
    }

}