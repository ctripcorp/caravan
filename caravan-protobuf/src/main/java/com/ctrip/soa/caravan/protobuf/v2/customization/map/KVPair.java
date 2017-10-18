package com.ctrip.soa.caravan.protobuf.v2.customization.map;

/**
 * Created by marsqing on 24/04/2017.
 */
public interface KVPair {

  Object key();

  Object value();

  void key(Object key);

  void value(Object value);

}
