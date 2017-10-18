package com.ctrip.soa.caravan.configuration.typed.corrected;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.Property;
import com.ctrip.soa.caravan.configuration.typed.DefaultTypedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedCorrectedProperty<T> extends DefaultTypedProperty<T> implements TypedCorrectedProperty<T> {

    private ValueCorrector<T> _valueCorrector;

    public DefaultTypedCorrectedProperty(Property property, ValueParser<T> valueParser, ValueCorrector<T> valueCorrector) {
        super(property, valueParser);
        NullArgumentChecker.DEFAULT.check(valueCorrector, "valueCorrector");
        _valueCorrector = valueCorrector;
    }

    @Override
    public T typedValue() {
        T value = super.typedValue();
        return _valueCorrector.correct(value);
    }
}