package com.ctrip.soa.caravan.protobuf.v2;

public class ProtobufConfig {

    private EnumMode enumMode;

    public EnumMode getEnumMode() {
        return enumMode;
    }

    public void setEnumMode(EnumMode enumMode) {
        this.enumMode = enumMode;
    }

    public enum EnumMode {
        ByOrdinal, ByValue
    }

}
