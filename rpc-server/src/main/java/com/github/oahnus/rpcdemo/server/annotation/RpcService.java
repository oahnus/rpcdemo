package com.github.oahnus.rpcdemo.server.annotation;

import com.github.oahnus.rpcdemo.api.RpcVersion;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by oahnus on 2019/7/12
 * 14:07.
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService {
    Class<?> value();
    RpcVersion version() default RpcVersion.V1_0;
}
