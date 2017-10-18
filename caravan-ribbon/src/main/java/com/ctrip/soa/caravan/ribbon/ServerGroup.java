package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.common.value.CollectionValues;
import com.ctrip.soa.caravan.common.value.MapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;

/**
 * Created by w.jian on 2017/2/25.
 */
@JsonPropertyOrder({"groupId", "weight", "servers", "metadata"})
public class ServerGroup {

    private String groupId;
    private Integer weight;
    private List<Server> servers;
    private List<Server> availableServers;
    private Map<String, String> metadata;

    public ServerGroup() {
        this(null, 0, null, null);
    }

    public ServerGroup(String groupId, Integer weight, List<Server> servers, Map<String, String> metadata) {
        this.groupId = groupId;
        this.weight = weight;
        this.servers = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.availableServers = new ArrayList<>();
        if (servers != null) {
            for (Server server : servers) {
                if (server == null || StringValues.isNullOrWhitespace(server.getServerId()))
                    continue;
                this.servers.add(server);
                if (server.isAlive()) {
                    this.availableServers.add(server);
                }
            }
        }
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
    }

    @JsonProperty
    public String getGroupId() {
        return groupId;
    }

    @JsonProperty
    public Integer getWeight() {
        return weight;
    }

    @JsonProperty
    public List<Server> getServers() {
        if (servers == null)
            servers = new ArrayList<>();
        return servers;
    }

    @JsonIgnore
    public List<Server> getAvailableServers() {
        return availableServers;
    }

    @JsonProperty
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return JacksonJsonSerializer.INSTANCE.serialize(this);
    }

    @Override
    public int hashCode() {
        if (StringValues.isNullOrWhitespace(getGroupId()))
            return 0;
        return getGroupId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ServerGroup)) {
            return false;
        }

        return equals((ServerGroup) obj);
    }

    public boolean equals(ServerGroup other) {

        if (other == null)
            return false;

        if (this == other) {
            return true;
        }

        if (!StringValues.equalsIgnoreCase(this.getGroupId(), other.getGroupId()))
            return false;

        if (!Objects.equals(this.getWeight(), other.getWeight()))
            return false;

        if (CollectionValues.isNullOrEmpty(this.getServers()))
            return CollectionValues.isNullOrEmpty(other.getServers());

        if (!Objects.equals(this.getServers(), other.getServers()))
            return false;

        if (MapValues.isNullOrEmpty(this.getMetadata()))
            return MapValues.isNullOrEmpty(other.getMetadata());

        return Objects.equals(this.getMetadata(), other.getMetadata());
    }

    public void refreshAvailableServers() {
        List<Server> newAvailableServers = new ArrayList<>();
        for (Server server : servers) {
            if (server.isAlive()) {
                newAvailableServers.add(server);
            }
        }
        availableServers = newAvailableServers;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String groupId;
        private Integer weight;
        private List<Server> servers = new ArrayList<>();
        private Map<String, String> metadata = null;

        public ServerGroup.Builder setGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public ServerGroup.Builder setWeight(Integer weight) {
            this.weight = weight;
            return this;
        }

        public ServerGroup.Builder setServers(List<Server> servers) {
            this.servers = servers;
            return this;
        }

        public ServerGroup.Builder setMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public ServerGroup build() {
            return new ServerGroup(groupId, weight, servers, metadata);
        }
    }
}
