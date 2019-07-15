package com.github.oahnus.rpcdemo.server.config;

import com.github.oahnus.rpcdemo.server.NioRpcServer;
import com.github.oahnus.rpcdemo.server.RpcServer;
import com.github.oahnus.rpcdemo.server.annotation.RpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by oahnus on 2019/7/12
 * 14:08.
 */
@Configuration
@ComponentScan(basePackages = {
        "com.github.oahnus.rpcdemo.server.annotation",
        "com.github.oahnus.rpcdemo.server.service",
})
public class SpringConfig {
//    @Bean
//    public NioRpcServer nioRpcServer() {
//        return new NioRpcServer();
//    }

    @Bean
    public RpcServer bioRpcServer() {
        return new RpcServer();
    }
}
