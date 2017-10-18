package com.ctrip.soa.caravan.configuration.dynamic;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.wrapper.PropertyWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultDynamicProperty extends PropertyWrapper implements DynamicProperty {

    private DynamicConfigurationManager _manager;

    public DefaultDynamicProperty(DynamicConfigurationManager manager, Property property) {
        super(property);
        NullArgumentChecker.DEFAULT.check(manager, "manager");
        _manager = manager;
    }

    @Override
    public void addChangeListener(PropertyChangeListener listener) {
        _manager.addPropertyChangeListener(key(), listener);
    }

}
