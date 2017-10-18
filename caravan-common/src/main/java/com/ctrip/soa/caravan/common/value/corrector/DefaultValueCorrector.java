package com.ctrip.soa.caravan.common.value.corrector;

import com.ctrip.soa.caravan.common.value.DefaultValues;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultValueCorrector<T> implements ValueCorrector<T> {
    
    private T _defaultValue;
    
    public DefaultValueCorrector(T defaultValue) {
        _defaultValue = defaultValue;
    }

    @Override
    public T correct(T value) {
        if (DefaultValues.isDefault(value))
            return _defaultValue;

        return value;
    }

}
