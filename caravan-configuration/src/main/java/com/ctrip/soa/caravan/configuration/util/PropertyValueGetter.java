package com.ctrip.soa.caravan.configuration.util;

import java.util.List;

import com.ctrip.soa.caravan.common.value.ArrayValues;
import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.configuration.typed.TypedProperty;
import com.google.common.collect.Lists;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PropertyValueGetter {

    private PropertyValueGetter() {

    }

    @SafeVarargs
    public static <T> T getTypedValue(TypedProperty<T>... hierarchicalProperties) {
        if (ArrayValues.isNullOrEmpty(hierarchicalProperties))
            return null;

        return getTypedValue(Lists.newArrayList(hierarchicalProperties));
    }

    private static <T> T getTypedValue(List<TypedProperty<T>> hierarchicalProperties) {
        if (CollectionValues.isNullOrEmpty(hierarchicalProperties))
            return null;

        if (hierarchicalProperties.size() == 1)
            return getTypedValue(hierarchicalProperties.get(0));

        if (hierarchicalProperties.size() == 2)
            return getTypedValue(hierarchicalProperties.get(0), hierarchicalProperties.get(1));

        T genericLevelPropertyValue = getTypedValue(hierarchicalProperties.remove(0));
        T concreteLevelPropertyValue = getTypedValue(hierarchicalProperties);
        return getTypedValue(genericLevelPropertyValue, concreteLevelPropertyValue);
    }

    private static <T> T getTypedValue(TypedProperty<T> genericLevelProperty, TypedProperty<T> concreteLevelProperty) {
        T genericLevelPropertyValue = getTypedValue(genericLevelProperty);
        T concreteLevelPropertyValue = getTypedValue(concreteLevelProperty);
        return getTypedValue(genericLevelPropertyValue, concreteLevelPropertyValue);
    }

    private static <T> T getTypedValue(TypedProperty<T> typedProperty) {
        return typedProperty == null ? null : typedProperty.typedValue();
    }

    private static <T> T getTypedValue(T genericLevelPropertyValue, T concreteLevelPropertyValue) {
        if (concreteLevelPropertyValue != null)
            return concreteLevelPropertyValue;

        return genericLevelPropertyValue;
    }

}
