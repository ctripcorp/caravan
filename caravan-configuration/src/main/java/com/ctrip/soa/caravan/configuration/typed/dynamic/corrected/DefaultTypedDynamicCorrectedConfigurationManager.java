package com.ctrip.soa.caravan.configuration.typed.dynamic.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.corrected.DynamicCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.corrected.DynamicCorrectedProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.DefaultTypedDynamicConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedDynamicCorrectedConfigurationManager extends DefaultTypedDynamicConfigurationManager
        implements TypedDynamicCorrectedConfigurationManager {

    public DefaultTypedDynamicCorrectedConfigurationManager(DynamicCorrectedConfigurationManager manager) {
        super(manager);
    }

    @Override
    protected DynamicCorrectedConfigurationManager manager() {
        return (DynamicCorrectedConfigurationManager) super.manager();
    }

    @Override
    public DynamicCorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector) {
        return manager().getProperty(key, valueCorrector);
    }

    @Override
    public <T> TypedDynamicCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser,
            ValueCorrector<T> valueCorrector) {
        return new DefaultTypedDynamicCorrectedProperty<>(getProperty(key), valueParser, valueCorrector);
    }

}
