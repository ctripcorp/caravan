package com.ctrip.soa.caravan.ribbon.serversource.manager;

/**
 * Created by w.jian on 2017/4/27.
 */
public interface ServerSourceRestoreListener {
    
    void onServerSourceRestore(ServerSourceRestoreEvent event);
}
