package com.github.oahnus.rpcdemo.server.service;

import com.github.oahnus.rpcdemo.api.HelloService;
import com.github.oahnus.rpcdemo.api.RpcVersion;
import com.github.oahnus.rpcdemo.server.annotation.RpcService;

/**
 * Created by oahnus on 2019/7/12
 * 14:11.
 */
@RpcService(value = HelloService.class, version = RpcVersion.V1_0)
public class HelloServiceImplV1 implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
