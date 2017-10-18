package com.ctrip.soa.caravan.common.value.corrector;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class RangeCorrector<T extends Comparable<T>> implements ValueCorrector<T> {

    private T _lowerBound;
    private T _upperBound;

    public RangeCorrector(T lowerBound, T upperBound) {
        _lowerBound = lowerBound;
        _upperBound = upperBound;
    }

    @Override
    public T correct(T value) {
        if (value == null)
            return null;

        if (value.compareTo(_lowerBound) < 0)
            return _lowerBound;

        if (value.compareTo(_upperBound) > 0)
            return _upperBound;

        return value;
    }

}
