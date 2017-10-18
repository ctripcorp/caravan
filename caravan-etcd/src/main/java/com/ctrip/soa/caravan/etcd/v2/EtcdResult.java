package com.ctrip.soa.caravan.etcd.v2;

import com.ctrip.soa.caravan.util.serializer.JacksonJsonSerializer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EtcdResult {

    private String action;
    private EtcdNode node;
    private EtcdNode prevNode;
    private Integer errorCode;
    private String message;
    private String cause;
    private long index;

    public EtcdResult() {

    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public EtcdNode getNode() {
        return node;
    }

    public void setNode(EtcdNode node) {
        this.node = node;
    }

    public EtcdNode getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(EtcdNode prevNode) {
        this.prevNode = prevNode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return JacksonJsonSerializer.INSTANCE.serialize(this);
    }

}
