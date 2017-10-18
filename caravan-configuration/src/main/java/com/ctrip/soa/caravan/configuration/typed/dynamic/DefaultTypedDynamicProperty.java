package com.ctrip.soa.caravan.configuration.typed.dynamic;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicProperty;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeListener;
import com.ctrip.soa.caravan.configuration.typed.DefaultTypedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedDynamicProperty<T> extends DefaultTypedProperty<T> implements TypedDynamicProperty<T> {

    public DefaultTypedDynamicProperty(DynamicProperty property, ValueParser<T> valueParser) {
        super(property, valueParser);
    }

    @Override
    protected DynamicProperty property() {
        return (DynamicProperty) super.property();
    }

    @Override
    public void addChangeListener(PropertyChangeListener listener) {
        property().addChangeListener(listener);
    }

}