package com.github.oahnus.rpcdemo.api;

import java.io.Serializable;

/**
 * Created by oahnus on 2019/7/12
 * 14:26.
 */
public enum RpcVersion implements Serializable {
    DEFAULT("1.0"),
    V1_0("v1.0"),
    V2_0("v2.0");

    private String version;

    RpcVersion(String version) {
        this.version = version;
    }

    public static RpcVersion getRpcVersion(String version) {
        for (RpcVersion type : values()) {
            if (type.version.equals(version)) {
                return type;
            }
        }
        return DEFAULT;
    }

    public String getVersion() {
        return this.version;
    }
}
