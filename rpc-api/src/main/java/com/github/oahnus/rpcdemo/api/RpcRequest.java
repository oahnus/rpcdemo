package com.github.oahnus.rpcdemo.api;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by oahnus on 2019/7/11
 * 14:55.
 */
@Data
public class RpcRequest implements Serializable {
    private String className;
    private String methodName;
    private Object[] args;
    private RpcVersion version;
}
