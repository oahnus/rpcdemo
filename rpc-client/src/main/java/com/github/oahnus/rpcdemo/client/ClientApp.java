package com.github.oahnus.rpcdemo.client;

import com.github.oahnus.rpcdemo.api.HelloService;
import com.github.oahnus.rpcdemo.client.config.SpringConfig;
import com.github.oahnus.rpcdemo.client.rpc.RpcClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by oahnus on 2019/7/12
 * 14:15.
 */
public class ClientApp {
    public static void main(String... args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        context.start();

        RpcClient rpcClient = context.getBean(RpcClient.class);
        HelloService helloService = rpcClient.getService(HelloService.class, "127.0.0.1", 7891);
        Thread thread1 = new Thread(() ->
                System.out.println(helloService.sayHello("Jerry")));
        Thread thread2 = new Thread(() ->
                System.out.println(helloService.sayHello("Tom")));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}
