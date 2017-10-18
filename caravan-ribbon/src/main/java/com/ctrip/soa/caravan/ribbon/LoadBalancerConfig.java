package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.ribbon.serversource.manager.ServerSourceRestoreListener;

/**
 * Created by w.jian on 2016/8/17.
 */
public class LoadBalancerConfig {
    private Func<LoadBalancerRule> rule;
    private Ping ping;
    private ServerSource serverSource;
    private ServerSourceRestoreListener serverSourceRestoreListener;
    private SeekRouteListener seekRouteListener;

    private LoadBalancerConfig(Func<LoadBalancerRule> rule,
                               Ping ping,
                               ServerSource serverSource,
                               ServerSourceRestoreListener listener,
                               SeekRouteListener seekRouteListener) {
        NullArgumentChecker.DEFAULT.check(serverSource, "serverSource");

        this.rule = rule;
        this.ping = ping;
        this.serverSource = serverSource;
        this.serverSourceRestoreListener = listener;
        this.seekRouteListener = seekRouteListener;
    }

    public Func<LoadBalancerRule> getRuleFactory() {
        return rule;
    }

    public Ping getPing() {
        return ping;
    }

    public ServerSource getServerSource() {
        return serverSource;
    }
    
    public ServerSourceRestoreListener getServerSourceRestoreListener() {
        return serverSourceRestoreListener;
    }
    
    public SeekRouteListener getSeekRouteListener() {
        return seekRouteListener;
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Func<LoadBalancerRule> rule = null;
        private Ping ping = null;
        private ServerSource serverSource;
        private ServerSourceRestoreListener serverSourceRestoreListener;
        private SeekRouteListener seekRouteListener;

        public Builder setRuleFactory(Func<LoadBalancerRule> rule) {
            this.rule = rule;
            return this;
        }

        public Builder setPing(Ping ping) {
            this.ping = ping;
            return this;
        }

        public Builder setServerSource(ServerSource serverSource) {
            this.serverSource = serverSource;
            return this;
        }
        
        public Builder setServerSourceRestoreListener(ServerSourceRestoreListener listener) {
            this.serverSourceRestoreListener = listener;
            return this;
        }

        public Builder setSeekRouteListener(SeekRouteListener seekRouteListener) {
            this.seekRouteListener = seekRouteListener;
            return this;
        }
    
        public LoadBalancerConfig build() {
            return new LoadBalancerConfig(rule, ping, serverSource, serverSourceRestoreListener, seekRouteListener);
        }
    }
}
