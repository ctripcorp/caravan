package com.ctrip.soa.caravan.common.value.corrector;

import com.ctrip.soa.caravan.common.value.DefaultValues;
import com.ctrip.soa.caravan.common.value.StringValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class NullValueRangeCorrector<T extends Comparable<T>> implements ValueCorrector<T> {

    private T _lowerBound;
    private T _upperBound;

    public NullValueRangeCorrector(T lowerBound, T upperBound) {
        _lowerBound = lowerBound;
        _upperBound = upperBound;
    }

    @Override
    public T correct(T value) {
        if (DefaultValues.isDefault(value))
            return null;

        if (value instanceof String && StringValues.isNullOrWhitespace((String) value))
            return null;

        if (value.compareTo(_lowerBound) < 0)
            return null;

        if (value.compareTo(_upperBound) > 0)
            return null;

        return value;
    }

}
