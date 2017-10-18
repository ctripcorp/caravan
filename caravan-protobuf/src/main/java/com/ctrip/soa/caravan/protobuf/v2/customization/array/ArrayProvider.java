package com.ctrip.soa.caravan.protobuf.v2.customization.array;

import com.ctrip.soa.caravan.protobuf.v2.customization.TypeCustomizationFactory;

/**
 * Created by marsqing on 20/04/2017.
 */
public interface ArrayProvider {

  @SuppressWarnings("rawtypes")
  TypeCustomizationFactory factoryForArrayOf(Class<?> elementClass);

}
