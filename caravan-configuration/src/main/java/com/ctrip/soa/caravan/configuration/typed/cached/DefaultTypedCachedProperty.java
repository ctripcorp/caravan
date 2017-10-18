package com.ctrip.soa.caravan.configuration.typed.cached;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.cached.CachedProperty;
import com.ctrip.soa.caravan.configuration.typed.DefaultTypedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedCachedProperty<T> extends DefaultTypedProperty<T> implements TypedCachedProperty<T> {

    protected volatile T _value;

    public DefaultTypedCachedProperty(CachedProperty property, ValueParser<T> valueParser) {
        super(property, valueParser);
        refresh();
    }

    @Override
    protected CachedProperty property() {
        return (CachedProperty) super.property();
    }

    @Override
    public T typedValue() {
        return _value;
    }

    @Override
    public void refresh() {
        property().refresh();
        _value = super.typedValue();
    }
}