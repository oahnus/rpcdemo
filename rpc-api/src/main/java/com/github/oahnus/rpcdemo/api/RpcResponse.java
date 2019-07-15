package com.github.oahnus.rpcdemo.api;

import lombok.Data;

/**
 * Created by oahnus on 2019/7/15
 * 11:39.
 */
@Data
public class RpcResponse {
    private Object resultVal;

    public static RpcResponse wrap(Object val) {
        RpcResponse resp = new RpcResponse();
        resp.setResultVal(val);
        return resp;
    }
}
