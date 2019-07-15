package com.github.oahnus.rpcdemo.client.config;

import com.github.oahnus.rpcdemo.client.rpc.RpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by oahnus on 2019/7/12
 * 14:16.
 */
@Configuration
public class SpringConfig {
    // Spring托管RpcClient
    @Bean("rpcClient")
    public RpcClient rpcClient() {
        return new RpcClient();
    }
}
