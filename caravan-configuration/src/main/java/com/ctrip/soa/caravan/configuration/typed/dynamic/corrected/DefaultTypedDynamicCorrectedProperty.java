package com.ctrip.soa.caravan.configuration.typed.dynamic.corrected;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.DynamicProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.DefaultTypedDynamicProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedDynamicCorrectedProperty<T> extends DefaultTypedDynamicProperty<T>
        implements TypedDynamicCorrectedProperty<T> {

    private ValueCorrector<T> _valueCorrector;

    public DefaultTypedDynamicCorrectedProperty(DynamicProperty property, ValueParser<T> valueParser,
            ValueCorrector<T> valueCorrector) {
        super(property, valueParser);
        NullArgumentChecker.DEFAULT.check(valueCorrector, "valueCorrector");
        _valueCorrector = valueCorrector;
    }

    @Override
    public T typedValue() {
        return _valueCorrector.correct(super.typedValue());
    }

}