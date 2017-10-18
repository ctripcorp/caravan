package com.ctrip.soa.caravan.configuration.typed.cached.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.cached.corrected.CachedCorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedProperty;
import com.ctrip.soa.caravan.configuration.typed.cached.DefaultTypedCachedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedCachedCorrectedConfigurationManager extends DefaultTypedCachedConfigurationManager implements TypedCachedCorrectedConfigurationManager {

    public DefaultTypedCachedCorrectedConfigurationManager(CachedCorrectedConfigurationManager manager) {
        super(manager);
    }
    
    @Override
    protected CachedCorrectedConfigurationManager manager() {
        return (CachedCorrectedConfigurationManager)super.manager();
    }

    @Override
    public CorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector) {
        return manager().getProperty(key, valueCorrector);
    }

    @Override
    public <T> TypedCachedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser,
            ValueCorrector<T> valueCorrector) {
        return new DefaultTypedCachedCorrectedProperty<>(getProperty(key), valueParser, valueCorrector);
    }
    
}
