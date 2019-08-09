package com.yuanshijia.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 * 定义一个自定义注解 @RpcInterface 当我们的项目接入 Spring 以后，Spring 扫描到这个注解之后，自动的通过我们的 ProxyFactory 创建代理对象，并存放在 spring 的 applicationContext 中。
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcInterface {
    

}
