package com.ctrip.soa.caravan.configuration.wrapper;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.Property;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PropertyWrapper implements Property {

    private Property _property;

    protected Property property() {
        return _property;
    }

    public PropertyWrapper(Property property) {
        NullArgumentChecker.DEFAULT.check(property, "property");
        _property = property;
    }

    @Override
    public String key() {
        return property().key();
    }

    @Override
    public String value() {
        return property().value();
    }

    @Override
    public String toString() {
        return property().toString();
    }

}
