package com.github.oahnus.rpcdemo.server.service;

import com.github.oahnus.rpcdemo.api.HelloService;
import com.github.oahnus.rpcdemo.api.RpcVersion;
import com.github.oahnus.rpcdemo.server.annotation.RpcService;

/**
 * Created by oahnus on 2019/7/12
 * 14:22.
 */
@RpcService(value = HelloService.class, version = RpcVersion.V2_0)
public class HelloServiceImplV2 implements HelloService {
    @Override
    public String sayHello(String name) {
        return "[v2] Hello " + name;
    }
}
