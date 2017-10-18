package com.ctrip.soa.caravan.ribbon;

import com.ctrip.soa.caravan.common.value.MapValues;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;
import java.util.Objects;

/**
 * Created by w.jian on 2016/6/14.
 */
@JsonPropertyOrder({"serverId", "isAlive", "metadata"})
public class Server {

    private String serverId;
    private boolean isAlive = true;
    private Map<String, String> metadata;

    public Server() { }

    public Server(String serverId, boolean isAlive, Map<String, String> metadata) {
        this.serverId = serverId;
        this.isAlive = isAlive;
        this.metadata = metadata;
    }

    @JsonProperty
    public String getServerId() {
        return serverId;
    }

    @JsonProperty
    public boolean isAlive() {
        return isAlive;
    }

    @JsonProperty
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    @JsonProperty
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return JacksonJsonSerializer.INSTANCE.serialize(this);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String serverId;
        private boolean isAlive = true;
        private Map<String, String> metadata;

        public Builder setServerId(String serverId) {
            this.serverId = serverId;
            return this;
        }

        public Builder setIsAlive(boolean isAlive) {
            this.isAlive = isAlive;
            return this;
        }

        public Builder setMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Server build() {
            return new Server(serverId, isAlive, metadata);
        }
    }

    @Override
    public int hashCode() {
        if (StringValues.isNullOrWhitespace(getServerId()))
            return 0;
        return getServerId().hashCode();
    }

    public boolean equals(Server other) {

        if (other == null)
            return false;

        if (this == other) {
            return true;
        }

        if (!StringValues.equalsIgnoreCase(this.getServerId(), other.getServerId()))
            return false;

        if (MapValues.isNullOrEmpty(this.getMetadata()))
            return MapValues.isNullOrEmpty(other.getMetadata());

        return Objects.equals(this.getMetadata(), other.getMetadata());
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Server)) {
            return false;
        }

        return equals((Server) obj);
    }
}
