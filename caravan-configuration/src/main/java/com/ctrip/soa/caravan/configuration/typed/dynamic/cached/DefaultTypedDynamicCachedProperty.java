package com.ctrip.soa.caravan.configuration.typed.dynamic.cached;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.cached.CachedPropertyWeakReferencePropertyChangeListener;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DynamicCachedProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.DefaultTypedDynamicProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedDynamicCachedProperty<T> extends DefaultTypedDynamicProperty<T> implements TypedDynamicCachedProperty<T> {

    protected volatile T _value;

    public DefaultTypedDynamicCachedProperty(DynamicCachedProperty property, ValueParser<T> valueParser) {
        super(property, valueParser);

        refresh();

        addChangeListener(new CachedPropertyWeakReferencePropertyChangeListener(this));
    }

    @Override
    protected DynamicCachedProperty property() {
        return (DynamicCachedProperty) super.property();
    }

    @Override
    public T typedValue() {
        return _value;
    }

    @Override
    public void refresh() {
        _value = super.typedValue();
    }

}
