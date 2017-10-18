package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.common.value.MapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;

/**
 * Created by w.jian on 2017/3/5.
 */
@JsonPropertyOrder({"routeId", "serverGroups"})
public class LoadBalancerRoute {

    private String routeId;
    private List<Server> servers;
    private List<ServerGroup> serverGroups;
    private HashMap<String, ServerGroup> serverGroupMap;

    public LoadBalancerRoute() { }

    public LoadBalancerRoute(String routeId, List<ServerGroup> serverGroups) {
        StringArgumentChecker.DEFAULT.check(routeId, "routeId");
        NullArgumentChecker.DEFAULT.check(serverGroups, "serverGroups");

        this.routeId = routeId;
        this.serverGroups = serverGroups;
    }

    @JsonProperty
    public String getRouteId() {
        return routeId;
    }

    @JsonIgnore
    public ServerGroup getServerGroup(String groupId) {
        if (serverGroupMap == null)
            buildFields();
        return serverGroupMap.get(groupId);
    }

    @JsonIgnore
    public List<Server> getServers() {
        if (servers == null)
            buildFields();
        return servers;
    }

    @JsonIgnore
    public List<Server> getAvailableServers() {
        List<Server> availableServers = new ArrayList<>();
        List<Server> allServers = getServers();
        for (Server server : allServers) {
            if (server.isAlive()) {
                availableServers.add(server);
            }
        }
        return availableServers;
    }

    @JsonProperty
    public List<ServerGroup> getServerGroups() {
        if (serverGroups == null)
            serverGroups = new ArrayList<>();
        return serverGroups;
    }

    @JsonIgnore
    public HashMap<String, ServerGroup> getServerGroupMap() {
        if (serverGroupMap == null)
            buildFields();
        return serverGroupMap;
    }

    @Override
    public String toString() {
        return JacksonJsonSerializer.INSTANCE.serialize(this);
    }

    @Override
    public int hashCode() {
        if (StringValues.isNullOrWhitespace(getRouteId()))
            return 0;
        return getRouteId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof LoadBalancerRoute))
            return false;
        return equals((LoadBalancerRoute)obj);
    }

    public boolean equals(LoadBalancerRoute other) {

        if (other == null)
            return false;

        if (this == other)
            return true;

        if (!StringValues.equalsIgnoreCase(this.getRouteId(), other.getRouteId()))
            return false;

        if (MapValues.isNullOrEmpty(this.getServerGroupMap()))
            return MapValues.isNullOrEmpty(other.getServerGroupMap());

        return Objects.equals(this.getServerGroupMap(), other.getServerGroupMap());
    }

    private void buildFields() {
        List<Server> newServers = new ArrayList<>();
        HashMap<String, ServerGroup> newServerGroupMap = new HashMap<>();
        if (serverGroups == null)
            serverGroups = new ArrayList<>();
        for (ServerGroup serverGroup : serverGroups) {
            if (serverGroup == null || StringValues.isNullOrWhitespace(serverGroup.getGroupId()))
                continue;
            
            newServers.addAll(serverGroup.getServers());
            newServerGroupMap.put(serverGroup.getGroupId(), serverGroup);
        }

        this.servers = newServers;
        this.serverGroupMap = newServerGroupMap;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String routeId;
        private List<ServerGroup> serverGroups;

        public Builder setRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public Builder setServerGroups(List<ServerGroup> serverGroups) {
            this.serverGroups = serverGroups;
            return this;
        }

        public LoadBalancerRoute build() {
            return new LoadBalancerRoute(routeId, serverGroups);
        }
    }
}
