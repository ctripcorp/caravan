package com.ctrip.soa.caravan.ribbon.serversource.monitor;

/**
 * Created by w.jian on 2016/7/26.
 */
public interface ServerStatusChangeListener {

    void onChange(ServerStatusChangeEvent e);
}
