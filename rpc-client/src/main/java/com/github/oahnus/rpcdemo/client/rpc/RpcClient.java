package com.github.oahnus.rpcdemo.client.rpc;

import com.github.oahnus.rpcdemo.api.RpcRequest;
import com.github.oahnus.rpcdemo.api.RpcVersion;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by oahnus on 2019/7/12
 * 14:17.
 */
public class RpcClient {
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz, String ip, int port){
        return (T) Proxy.newProxyInstance(RpcClient.class.getClassLoader(),new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest request = new RpcRequest();
                request.setClassName(clazz.getName());
                request.setMethodName(method.getName());
                request.setArgs(args);
                request.setVersion(RpcVersion.V1_0);

                // NIO
//                return RpcSender.sendNioRpcRequest(ip, port, request);
                return RpcSender.sendRpcRequest(ip, port, request);
            }
        });
    }

}
