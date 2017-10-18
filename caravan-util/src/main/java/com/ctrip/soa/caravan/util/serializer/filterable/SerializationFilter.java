package com.ctrip.soa.caravan.util.serializer.filterable;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface SerializationFilter {

    Object filter(FilterableProperty property, Object value);

}
