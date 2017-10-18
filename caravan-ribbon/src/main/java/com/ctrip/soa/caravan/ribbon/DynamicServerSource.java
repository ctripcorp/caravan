package com.ctrip.soa.caravan.ribbon;

/**
 * Created by w.jian on 2016/7/18.
 */
public interface DynamicServerSource extends ServerSource {

    void registerServerSourceChangeListener(ServerSourceChangeListener listener);
}
