package com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.cached.corrected.DynamicCachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.dynamic.cached.corrected.DynamicCachedCorrectedProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.DefaultTypedDynamicCachedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedDynamicCachedCorrectedConfigurationManager extends
        DefaultTypedDynamicCachedConfigurationManager implements TypedDynamicCachedCorrectedConfigurationManager {

    public DefaultTypedDynamicCachedCorrectedConfigurationManager(DynamicCachedCorrectedConfigurationManager manager) {
        super(manager);
    }

    @Override
    protected DynamicCachedCorrectedConfigurationManager manager() {
        return (DynamicCachedCorrectedConfigurationManager) super.manager();
    }

    @Override
    public DynamicCachedCorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector) {
        return manager().getProperty(key, valueCorrector);
    }

    @Override
    public <T> TypedDynamicCachedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser,
            ValueCorrector<T> valueCorrector) {
        return new DefaultTypedDynamicCachedCorrectedProperty<>(getProperty(key), valueParser, valueCorrector);
    }

}
