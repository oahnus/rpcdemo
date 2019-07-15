package com.github.oahnus.rpcdemo.server;

import com.github.oahnus.rpcdemo.api.RpcVersion;
import com.github.oahnus.rpcdemo.server.annotation.RpcService;
import com.github.oahnus.rpcdemo.server.process.Processor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by oahnus on 2019/7/12
 * 14:09.
 */
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {
    private static final int PORT = 7891;
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    // Rpc服务容器
    private Map<String, Object> handlerMap = new HashMap<>();

    public void afterPropertiesSet() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("BIO RPC Server Start Listening On Port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Receive RPC Request From IP:PORT " + socket.getRemoteSocketAddress());
                threadPool.execute(new Processor(socket, handlerMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取注解RpcService的Bean
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!beans.isEmpty()) {
            for (Object bean : beans.values()) {
                // 将Bean注册到容器中
                RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
                Class<?> rpcServiceClass = rpcService.value();
                RpcVersion rpcVersion = rpcService.version();

                String clazzName = rpcServiceClass.getName();
                String key = clazzName + "-" + rpcVersion.getVersion();
                handlerMap.put(key, bean);
            }
        }
    }
}
