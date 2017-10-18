package com.ctrip.soa.caravan.util.safelist;

import java.util.List;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.parser.ValueParser;
import com.ctrip.soa.caravan.configuration.facade.TypedDynamicCachedCorrectedProperties;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SafeListManagerConfig<T> {

    private TypedDynamicCachedCorrectedProperties _properties;
    private ValueParser<List<T>> _valueParser;

    public SafeListManagerConfig(TypedDynamicCachedCorrectedProperties properties, ValueParser<List<T>> valueParser) {
        NullArgumentChecker.DEFAULT.check(properties, "properties");
        NullArgumentChecker.DEFAULT.check(valueParser, "valueParser");
        _properties = properties;
        _valueParser = valueParser;
    }

    public TypedDynamicCachedCorrectedProperties properties() {
        return _properties;
    }

    public ValueParser<List<T>> valueParser() {
        return _valueParser;
    }

}
