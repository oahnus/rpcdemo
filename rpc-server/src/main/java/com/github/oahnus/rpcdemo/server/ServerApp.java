package com.github.oahnus.rpcdemo.server;

import com.github.oahnus.rpcdemo.server.config.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by oahnus on 2019/7/12
 * 14:07.
 */
public class ServerApp {
    public static void main(String... args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        context.start();
    }
}
