package com.ctrip.soa.caravan.common.value;

import com.ctrip.soa.caravan.common.delegate.Func1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class MapValues {

    private MapValues() {

    }

    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <K, V> Map<K, V> from(Collection<V> collection, Func1<V, K> keySelector) {
        HashMap<K, V> result = new HashMap<>();
        for (V item : collection) {
            result.put(keySelector.execute(item), item);
        }
        return result;
    }

    public static <S, K, V> Map<K, V> from(Collection<S> collection, Func1<S, K> keySelector, Func1<S, V> valueSelector) {
        HashMap<K, V> result = new HashMap<>();
        for (S item : collection) {
            result.put(keySelector.execute(item), valueSelector.execute(item));
        }
        return result;
    }

    public static <K, V> Map<K, V> from(V[] array, Func1<V, K> keySelector) {
        HashMap<K, V> result = new HashMap<>();
        for (V item : array) {
            result.put(keySelector.execute(item), item);
        }
        return result;
    }

    public static <S, K, V> Map<K, V> from(S[] array, Func1<S, K> keySelector, Func1<S, V> valueSelector) {
        HashMap<K, V> result = new HashMap<>();
        for (S item : array) {
            result.put(keySelector.execute(item), valueSelector.execute(item));
        }
        return result;
    }
}
