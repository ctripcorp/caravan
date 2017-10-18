package com.ctrip.soa.caravan.configuration.typed.dynamic;

import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicProperty;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeListener;
import com.ctrip.soa.caravan.configuration.typed.DefaultTypedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedDynamicConfigurationManager extends DefaultTypedConfigurationManager
        implements TypedDynamicConfigurationManager {

    public DefaultTypedDynamicConfigurationManager(DynamicConfigurationManager manager) {
        super(manager);
    }

    @Override
    protected DynamicConfigurationManager manager() {
        return (DynamicConfigurationManager) super.manager();
    }

    @Override
    public <T> TypedDynamicProperty<T> getProperty(String key, ValueParser<T> valueParser) {
        return new DefaultTypedDynamicProperty<T>(getProperty(key), valueParser);
    }

    @Override
    public DynamicProperty getProperty(String key) {
        return manager().getProperty(key);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        manager().addPropertyChangeListener(listener);

    }

    @Override
    public void addPropertyChangeListener(String key, PropertyChangeListener listener) {
        manager().addPropertyChangeListener(key, listener);
    }

}
