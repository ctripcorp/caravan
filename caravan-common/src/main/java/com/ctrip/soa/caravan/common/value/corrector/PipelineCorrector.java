package com.ctrip.soa.caravan.common.value.corrector;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PipelineCorrector<T> implements ValueCorrector<T> {

    private List<ValueCorrector<T>> _valueCorrectors;

    @SafeVarargs
    public PipelineCorrector(ValueCorrector<T>... valueCorrectors) {
        _valueCorrectors = Arrays.asList(valueCorrectors);
    }

    public PipelineCorrector(List<ValueCorrector<T>> valueCorrectors) {
        _valueCorrectors = valueCorrectors;
    }

    @Override
    public T correct(T value) {
        for (ValueCorrector<T> valueCorrector : _valueCorrectors) {
            value = valueCorrector.correct(value);
        }

        return value;
    }

}
