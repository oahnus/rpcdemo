package com.github.oahnus.rpcdemo.server.process;

import com.github.oahnus.rpcdemo.api.RpcRequest;
import com.github.oahnus.rpcdemo.api.RpcResponse;
import com.github.oahnus.rpcdemo.api.RpcVersion;
import com.github.oahnus.rpcdemo.api.SerializeUtil;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.util.Map;

/**
 * Created by oahnus on 2019/7/11
 * 23:43.
 */
public class Processor implements Runnable, ProcessHandler {
    private Socket socket;
    private Map<String, Object> handlerMap;

    public Processor(Socket socket, Map<String, Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        OutputStream out = null;
        try {
            InputStream in = socket.getInputStream();
            byte[] reqBytes = SerializeUtil.readInputStream(in);
            // decode
            RpcRequest request = SerializeUtil.deserialize(reqBytes, RpcRequest.class);
            // Run Rpc Method
            RpcResponse response = handle(request);

            byte[] respBytes = SerializeUtil.serialize(response, RpcResponse.class);

            out = socket.getOutputStream();
            out.write(respBytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RpcResponse handle(RpcRequest request) {
        try {
            String className = request.getClassName();
            String methodName = request.getMethodName();
            Object[] args = request.getArgs();
            RpcVersion rpcVersion = request.getVersion();

            Class[] methodArgTypes = new Class[args.length];
            for (int i=0; i<args.length; i++) {
                methodArgTypes[i] = args[i].getClass();
            }

            String key = className + "-" + rpcVersion.getVersion();
            Object bean = handlerMap.get(key);

            if (bean == null) {
                throw new RuntimeException("Service Not Found");
            }

            Method method = bean.getClass().getMethod(methodName, methodArgTypes);

            System.out.println("Execute Method: " + methodName);
            Object result = method.invoke(bean, args);
            System.out.println("Method Return: " + result);
            return RpcResponse.wrap(result);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}