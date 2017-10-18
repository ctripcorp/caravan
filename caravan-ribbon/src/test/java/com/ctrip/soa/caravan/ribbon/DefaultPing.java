package com.ctrip.soa.caravan.ribbon;

/**
 * Created by w.jian on 2016/12/24.
 */
public class DefaultPing implements Ping {

    private boolean success;

    public DefaultPing() { }

    public DefaultPing(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isAlive(Server server) {
        return isSuccess();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
