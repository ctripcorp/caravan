package com.ctrip.soa.caravan.configuration.typed.dynamic.cached.corrected;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.corrector.ValueCorrector;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.dynamic.cached.DynamicCachedProperty;
import com.ctrip.soa.caravan.configuration.typed.dynamic.cached.DefaultTypedDynamicCachedProperty;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultTypedDynamicCachedCorrectedProperty<T> extends DefaultTypedDynamicCachedProperty<T>
        implements TypedDynamicCachedCorrectedProperty<T> {

    private ValueCorrector<T> _valueCorrector;

    public DefaultTypedDynamicCachedCorrectedProperty(DynamicCachedProperty property, ValueParser<T> valueParser,
            ValueCorrector<T> valueCorrector) {
        super(property, valueParser);
        NullArgumentChecker.DEFAULT.check(valueCorrector, "valueCorrector");
        _valueCorrector = valueCorrector;
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        if (null != _valueCorrector)
            _value = _valueCorrector.correct(_value);
    }

}