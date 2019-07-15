package com.github.oahnus.rpcdemo.server.process;

import com.github.oahnus.rpcdemo.api.RpcRequest;
import com.github.oahnus.rpcdemo.api.RpcResponse;
import com.github.oahnus.rpcdemo.api.RpcVersion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by oahnus on 2019/7/15
 * 14:34.
 */
public class NioProcessor implements ProcessHandler{
    private Map<String, Object> handlerMap;

    public NioProcessor(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public RpcResponse handle(RpcRequest request) {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Object[] args = request.getArgs();
        RpcVersion rpcVersion = request.getVersion();

        Class[] methodArgTypes = new Class[args.length];
        for (int i=0; i<args.length; i++) {
            methodArgTypes[i] = args[i].getClass();
        }

        // Get Rpc Handler Bean
        String key = className + "-" + rpcVersion.getVersion();
        Object bean = handlerMap.get(key);

        if (bean == null) {
            throw new RuntimeException("Service Not Found");
        }

        Object result = null;
        try {
            Method method = bean.getClass().getMethod(methodName, methodArgTypes);
            System.out.println("Execute Method: " + methodName);
            result = method.invoke(bean, args);
            System.out.println("Method Return: " + result);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return RpcResponse.wrap(result);
    }
}
