package com.ctrip.soa.caravan.ribbon.serversource.filter;

/**
 * Created by w.jian on 2016/7/26.
 */
public interface ServerContextChangeListener {

    void onChange(ServerContextChangeEvent event);
}
