package com.ctrip.soa.caravan.configuration.typed.corrected;

import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedConfigurationManager;
import com.ctrip.soa.caravan.configuration.corrected.CorrectedProperty;
import com.ctrip.soa.caravan.configuration.typed.DefaultTypedConfigurationManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedCorrectedConfigurationManager extends DefaultTypedConfigurationManager
        implements TypedCorrectedConfigurationManager {

    public DefaultTypedCorrectedConfigurationManager(CorrectedConfigurationManager manager) {
        super(manager);
    }

    @Override
    protected CorrectedConfigurationManager manager() {
        return (CorrectedConfigurationManager) super.manager();
    }

    @Override
    public CorrectedProperty getProperty(String key, ValueCorrector<String> valueCorrector) {
        return manager().getProperty(key, valueCorrector);
    }

    @Override
    public <T> TypedCorrectedProperty<T> getProperty(String key, ValueParser<T> valueParser,
            ValueCorrector<T> valueCorrector) {
        return new DefaultTypedCorrectedProperty<>(manager().getProperty(key), valueParser, valueCorrector);
    }

}
