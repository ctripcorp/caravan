package com.ctrip.soa.caravan.ribbon;

/**
 * Created by w.jian on 2016/6/14.
 */
public interface Ping {

    boolean isAlive(Server server);
}
