package com.ctrip.soa.caravan.ribbon.serversource.monitor;

/**
 * Created by w.jian on 2016/6/21.
 */
public interface ServerSourceMonitor {

    void monitorServers();

    boolean isMonitoring();

    void addServerStatusChangeListener(ServerStatusChangeListener listener);
}
