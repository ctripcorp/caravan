package com.ctrip.soa.caravan.common.collect;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class KeyValuePair<K, V> {

    private K _key;
    private V _value;

    public KeyValuePair() {

    }

    public KeyValuePair(K key, V value) {
        _key = key;
        _value = value;
    }

    public K getKey() {
        return _key;
    }

    public void setKey(K key) {
        _key = key;
    }

    public V getValue() {
        return _value;
    }

    public void setValue(V value) {
        _value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyValuePair<?, ?> that = (KeyValuePair<?, ?>) o;

        if (_key != null ? !_key.equals(that._key) : that._key != null) {
            return false;
        }
        return _value != null ? _value.equals(that._value) : that._value == null;
    }

    @Override
    public int hashCode() {
        int result = _key != null ? _key.hashCode() : 0;
        result = 31 * result + (_value != null ? _value.hashCode() : 0);
        return result;
    }
}
