package com.ctrip.soa.caravan.configuration.defaultvalue;

import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.wrapper.PropertyWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultValueWrapper extends PropertyWrapper {

    private String _defaultValue;

    public DefaultValueWrapper(Property property, String defaultValue) {
        super(property);
        _defaultValue = defaultValue;
    }

    @Override
    public String value() {
        String value = super.value();
        if (value == null)
            return _defaultValue;
        return value;
    }
}
