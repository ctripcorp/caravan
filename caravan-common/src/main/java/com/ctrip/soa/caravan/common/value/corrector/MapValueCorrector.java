package com.ctrip.soa.caravan.common.value.corrector;

import java.util.HashMap;
import java.util.Map;

import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class MapValueCorrector<K, V> implements ValueCorrector<Map<K, V>> {

    private ValueCorrector<V> _valueCorrector;

    public MapValueCorrector(ValueCorrector<V> valueCorrector) {
        NullArgumentChecker.DEFAULT.check(valueCorrector, "valueCorrector");
        _valueCorrector = valueCorrector;
    }

    @Override
    public Map<K, V> correct(Map<K, V> value) {
        if (value == null)
            return null;

        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, V> item : value.entrySet()) {
            result.put(item.getKey(), _valueCorrector.correct(item.getValue()));
        }

        return result;
    }

}
