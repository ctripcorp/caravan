package com.ctrip.soa.caravan.configuration.corrected;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.wrapper.PropertyWrapper;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultCorrectedProperty extends PropertyWrapper implements CorrectedProperty {

    private ValueCorrector<String> _valueCorrector;

    public DefaultCorrectedProperty(Property property, ValueCorrector<String> valueCorrector) {
        super(property);
        NullArgumentChecker.DEFAULT.check(valueCorrector, "valueCorrector");
        _valueCorrector = valueCorrector;
    }

    @Override
    public String value() {
        return _valueCorrector.correct(super.value());
    }

}
