package com.github.oahnus.rpcdemo.server.process;

import com.github.oahnus.rpcdemo.api.RpcRequest;
import com.github.oahnus.rpcdemo.api.RpcResponse;

/**
 * Created by oahnus on 2019/7/15
 * 14:35.
 */
public interface ProcessHandler {
    RpcResponse handle(RpcRequest request);
}
